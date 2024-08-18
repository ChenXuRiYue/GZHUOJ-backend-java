package com.gzhuoj.contest.util;

import com.gzhuoj.contest.model.pojo.UpdateScoreAttempt;
import com.gzhuoj.contest.model.pojo.CompetitorBasicInfo;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import com.gzhuoj.contest.model.pojo.PersonalSingleProblemResults;
import common.enums.SubmissionStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class BoardUtil {
     private final RedisTemplate<String , Object> redisTemplate;
     private final ZSetOperations<String, String> zSetOperations;
     private final HashOperations<String, String, Object> hashOperations;

    public BoardUtil(RedisTemplate<String, Object> redisTemplate,
                     ZSetOperations<String, String> zSetOperations,
                     HashOperations<String, String, Object> hashOperations) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = zSetOperations;
        this.hashOperations = hashOperations;
    }

    /**
     * 重命名
     */
    public String renameKey(String contestId, CompetitorBasicInfo competitor) {
        return contestId + "->" + competitor.getAccount();
    }
    /**
     * 分数哈希 ，三个统计维度。 与下方函数比较，该函数不是增加，而是直接插入。 用于防止缓存出现问题，导致重新启动要进行批量数据插入
     * x : 过题
     * y : 每道题首次统过时间
     * z : 已通过题目的罚时提交次数
     * f = x * (10 ^ 12) + y * 20 + z
     */
    public Double scoreHash(Integer x , Integer y, Integer z){
        return Math.pow(10 , 12) * x + 20.0 * y + z;
    }
    public Double scoreHash(Integer x, Integer y){
        return Math.pow(10, 12) + x * 20.0 + y;
    }

    /**
     * 比赛开始时，进行一些必要的初始化
     */
    public void initBoard(String contestId, List<CompetitorBasicInfo> competitors, Integer durations, TimeUnit timeUnit){
        // 设置过期时间。
        for(CompetitorBasicInfo competitor: competitors){
            zSetOperations.add(contestId, competitor.getAccount(),  0);
            PersonalScore personalScore = PersonalScore.builder()
                    .competitor(competitor)
                    // 初始每个人都是排名第一。
                    .standing(1)
                    .problemResultsDetails(new HashMap<>())
                    .dirt("0%")
                    .penalty(0L)
                    .build();
            hashOperations.put(contestId, competitor.getAccount(), personalScore);
        }
        // 统一设置数据结构过期时间
        redisTemplate.expire(contestId ,  durations , timeUnit);
    }
    /**
     * result 判定
     * 0 判断为失败
     * 1。 通过积分
     */
    public Integer judgeResult(SubmissionStatus result) {
        if (Objects.equals(result, SubmissionStatus.ACCEPTED)) {
            return 1;
        } else if (SubmissionStatus.WRONG_ANSWER.equals(result) ||
                SubmissionStatus.PRESENTATION_ERROR.equals(result) ||
                SubmissionStatus.TIME_LIMIT_EXCEED.equals(result) ||
                SubmissionStatus.MEMORY_LIMIT_EXCEED.equals(result) ||
                SubmissionStatus.RUNTIME_ERROR.equals(result) ||
                SubmissionStatus.OUTPUT_LIMIT_EXCEED.equals(result)) {
            return 0;
        } else return 2;
    }

    /**
     * 要禁止比赛过程中修改编号。 否则会出现bug
     * 封装一个内容
     * passTime : 首次通过的时间。 PunishTime 罚时次数。
     */


    public Boolean updateBoardAttempt(UpdateScoreAttempt param) {
        int checkResult = judgeResult(param.getSubmissionStatus());
        if (checkResult == 0) {
            reject(param);
            return false;
        } else if (checkResult == 1) {
            accepts(param);
            return true;
        } else return false;
    }

    /**
     * TODO 和时间相关的字段还没有处理。
     *
     * @param param
     */
    public void accepts(UpdateScoreAttempt param){
        PersonalScore personalScore = (PersonalScore) hashOperations.get(param.getContestId(), param.getCompetitor().getAccount());
        // 保险起见必须触发重新插入机制。 TODO 要防止一些极端情况。
        if(ObjectUtils.isEmpty(personalScore)){

        }
        // 判断当前题目是否已经通过。
        assert personalScore != null;
        Map<String, PersonalSingleProblemResults> problemResultDetails = personalScore.getProblemResultsDetails();
        // 得到对应题目的保存结果
        PersonalSingleProblemResults record = problemResultDetails.get(param.getProblemId());
        // 当前题目已经通过
        if(!ObjectUtils.isEmpty(record) && record.getStatus().equals(true)){
            return;
        } else if(ObjectUtils.isEmpty(record)){
            // TODO 测试的时候处理时间。
            record = PersonalSingleProblemResults.builder()
                    .problemId(param.getProblemId())
                    .status(true)
                    .times(System.currentTimeMillis())
                    .build();
            // 插入记录
            hashOperations.put(param.getContestId(), param.getCompetitor().getAccount(), record);
        }else {
            record.pass();
        }
        // 完成校验，判定为有效提交。 更新排行榜排名。
        Double score = scoreHash(param.getPunishTime(), param.getPassTime());
        zSetOperations.add(param.getContestId(),
                param.getCompetitor().getAccount(), score);
    }
    public void reject(UpdateScoreAttempt param){
        var personalScore = (PersonalScore) hashOperations.get(param.getContestId(), param.getCompetitor().getAccount());
        // 保险起见必须触发重新插入机制。 TODO 要防止一些极端情况。
        if(ObjectUtils.isEmpty(personalScore)){

        }
        // 判断当前题目是否已经通过。
        assert personalScore != null;
        Map<String, PersonalSingleProblemResults> problemResultDetails = personalScore.getProblemResultsDetails();
        // 得到对应题目的保存结果
        PersonalSingleProblemResults record = problemResultDetails.get(param.getProblemId());
        // 当前题目已经通过 不做处理
        if(!ObjectUtils.isEmpty(record) && record.getStatus().equals(true)){
            return;
        } else if(ObjectUtils.isEmpty(record)){
            // TODO 测试的时候处理时间
            record = PersonalSingleProblemResults.builder()
                    .problemId(param.getProblemId())
                    .status(false)
                    .penaltyCount(1)
                    .times(System.currentTimeMillis())
                    .build();
            hashOperations.put(param.getContestId(), param.getCompetitor().getAccount(), record);
        }else {
            record.reject();
        }
        // 完成校验，判定为有效提交。
    }

    /**
     *   查相关的API
     *   1. 查出给定范围，并且给出列表。
     */
    public List<PersonalScore> rangeViewByLimit(String contestId,  Integer l , Integer r){
        List<String> competitors = new ArrayList<>(Objects.requireNonNull(zSetOperations.range(contestId, l, r)));
        competitors.sort(Comparator.comparingDouble(key -> {
            Double target =  zSetOperations.score(contestId, key);
            return ObjectUtils.isEmpty(target)? 0.0 : target;
        }));
        return competitors.stream().map(x -> (PersonalScore)hashOperations.get(contestId, x)).collect(Collectors.toList());
    }
}
