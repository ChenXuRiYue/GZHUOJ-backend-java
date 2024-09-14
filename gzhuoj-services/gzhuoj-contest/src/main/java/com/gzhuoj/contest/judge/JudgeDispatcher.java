package com.gzhuoj.contest.judge;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import common.constant.RedisKey;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.model.entity.SubmitDO;
import common.redis.RedisUtil;
import common.enums.SubmissionStatus;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_SUBMIT_ERROR;

/**
 * 评测调度
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JudgeDispatcher {
    private final StringRedisTemplate stringRedisTemplate;
    private final JudgeDispenser judgeDispenser;
    private final SubmitMapper submitMapper;

    /**
     * 发送评测任务
     * @param submitId 提交Id
     * @param problemNum 题目Id
     */
    @Transactional
    public void sendTask(Integer submitId, Integer problemNum) {
        SubmitDO submitDO = new SubmitDO();
        submitDO.setSubmitId(submitId);
        LambdaUpdateWrapper<SubmitDO> updateWrapper = Wrappers.lambdaUpdate(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, submitId);
        try {
            // 将提交Id存入redis的List中充当全局任务调度队列
            boolean hasPush = stringRedisTemplate.opsForList().leftPush(RedisKey.CONTEST_JUDGE_QUEUE, submitId.toString()) != null;
            if (!hasPush) {
                // 提交到评测队列失败，更新提交状态
                updateWrapper.set(SubmitDO::getStatus, SubmissionStatus.STATUS_SUBMITTED_FAILED);
                submitMapper.update(submitDO, updateWrapper);
                throw new ClientException(JUDGE_SUBMIT_ERROR);
            }
            // 正常则将评测任务进行派发
            judgeDispenser.processWaitingTasks();
        } catch (Exception ex) {
            log.error("提交ID: {} 的判题任务处理异常: {}", submitId, ex.getMessage(), ex);
            // 将提交状态设置为异常错误而不是直接错误
            updateWrapper.set(SubmitDO::getStatus, SubmissionStatus.STATUS_SYSTEM_ERROR);
            submitMapper.update(null, updateWrapper);
        }
    }
}
