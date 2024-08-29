package common.redis;

import static common.constant.RedisKey.REGULAR_CONTEST;

// 用于构造、获取redis中 key， hashKey，等各种缓存场景下的工具类
public class RedisKeyUtil {

    /**
     * 生成竞赛题目集的基础信息： 题目列表，和数据拆开。作为缓存
     * @param contestId
     * @return
     */
    public static String generateContestProblemSetKey(Integer contestId){
        return REGULAR_CONTEST  + contestId;
    }
}
