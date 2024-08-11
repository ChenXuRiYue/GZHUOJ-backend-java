package com.gzhuoj.contest.judge;

import com.gzhuoj.contest.constant.RedisKey;
import com.gzhuoj.contest.constant.enums.JudgeType;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.pojo.ToJudgeDTO;
import com.gzhuoj.contest.service.SubmitService;
import com.gzhuoj.contest.util.RedisUtil;
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
                RedisKey.CONTEST_JUDGE_QUEUE
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
            toJudgeDTO.setSubmitDO(submitDO);
            // 根据JudgeType 指定评测类型
            dispatcher.dispatch(JudgeType.COMMON_JUDGE, toJudgeDTO);
        }
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
