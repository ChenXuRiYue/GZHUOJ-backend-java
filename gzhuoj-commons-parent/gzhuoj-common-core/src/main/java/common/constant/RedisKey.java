package common.constant;

import lombok.Data;

/**
 * Redis Key 定义常量类
 */
public final class RedisKey {
    /**
     * 标准比赛题目集主页
     */
    // Key
    public static final String REGULAR_CONTEST_PROBLEM_SET = "index-gzhuoj-contest-service:regular_contest_problem_set:";


    public static final String REGULAR_CONTEST = "regular_contest:";


    // hashKey
    public static final String REGULAR_CONTEST_PROBLEM_SET_HASH_KEY = "regular_contest_problem_set_hash_key";


    public static final String REGULAR_CONTEST_PROBLEMS = "regular_contest_problem";
    public static final String TEAM_LOGIN_KEY = "gzhuoj-team:login:%s";
    public static final String CONTEST_JUDGE_QUEUE = "gzhuoj-contest-judge:queue";

}
