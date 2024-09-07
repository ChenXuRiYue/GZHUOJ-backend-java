package com.gzhuoj.contest.judge;

import cn.hutool.core.bean.BeanUtil;
import com.gzhuacm.sdk.contest.model.dto.SubmitDTO;
import common.constant.JudgeType;
import common.constant.RedisKey;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuacm.sdk.contest.model.dto.ToJudgeDTO;
import com.gzhuoj.contest.service.judge.SubmitService;
import common.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 评测派发
 */
@RequiredArgsConstructor
@Component
public class JudgeDispenser extends AbstractDispenser {
    private final StringRedisTemplate stringRedisTemplate;
    private final SubmitService submitService;
    private final Dispatcher dispatcher;

    public void processWaitingTasks() {
        // 目前还未加入普遍评测接口，但需为其优先级进行排序
        priorityTask(
                RedisKey.CONTEST_JUDGE_QUEUE,
                RedisKey.COMMON_JUDGE_QUEUE
        );
    }

    @Override
    public void dispenserTask(String taskStr) {
        Integer submitId = Integer.parseInt(taskStr);
        SubmitDO submitDO = submitService.getSubmitDO(submitId);
        if(submitDO != null){
            // 对提交进行派送调度
            // 当前默认只有比赛评测
            ToJudgeDTO toJudgeDTO = new ToJudgeDTO();
            SubmitDTO submitDTO = getSubmitDTO(submitDO);
            toJudgeDTO.setSubmitDTO(submitDTO);
            // 根据JudgeType 指定评测类型
            dispatcher.dispatch(JudgeType.COMMON_JUDGE, toJudgeDTO);
        }
        processWaitingTasks();
    }

    private static SubmitDTO getSubmitDTO(SubmitDO submitDO) {
        return BeanUtil.toBean(submitDO, SubmitDTO.class);
    }

    @Override
    public String getTaskByRedis(String queue) {
        RedisUtil redisUtil = new RedisUtil(stringRedisTemplate);
        long listSize = redisUtil.getListSize(queue);
        if (listSize > 0) {
            return (String) redisUtil.rPop(queue);
        }
        return null;
    }
}
