package com.gzhuoj.contest.judge;

import com.gzhuoj.contest.constant.RedisKey;
import com.gzhuoj.contest.constant.enums.JudgeType;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.pojo.ToJudgeDTO;
import com.gzhuoj.contest.service.judge.SubmitService;
import com.gzhuoj.contest.util.RedisUtil;
import common.enums.SubmitWaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 评测派发
 */
@RequiredArgsConstructor
@Component
public class JudgeDispenser extends AbstractDispenser {
    private final StringRedisTemplate stringRedisTemplate;
    private final SubmitService submitService;
    private final Dispatcher dispatcher;

    @Async("judgeTaskAsyncPool")
    public void processWaitingTasks() {
        // 优先级 比赛 -> 非比赛提交 -> 在线测试
        priorityTask(
                SubmitWaitingQueue.CONTEST_JUDGE_QUEUE,
                SubmitWaitingQueue.COMMON_JUDGE_QUEUE,
                SubmitWaitingQueue.ONLINE_TEST_JUDGE_QUEUE
        );
    }

    @Override
    public void dispenserTask(String path, String taskStr) {
        Integer submitId = Integer.parseInt(taskStr);
        SubmitDO submitDO = submitService.getSubmitDO(submitId);
        if(submitDO != null){
            // 对提交进行派送调度
            ToJudgeDTO toJudgeDTO = new ToJudgeDTO();
            toJudgeDTO.setSubmitDO(submitDO);
            // 根据JudgeType 指定评测类型
            dispatcher.dispatch(JudgeType.COMMON_JUDGE, toJudgeDTO);
        }
        processWaitingTasks();
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
