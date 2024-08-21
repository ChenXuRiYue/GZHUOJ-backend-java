package com.gzhuoj.contest.constant;

import lombok.Data;

/**
 * Redis Key 定义常量类
 */
public final class RedisKey {
    /**
     * 标准比赛题目集主页
     */
    public static final String REGULAR_CONTEST_PROBLEM_SET = "index-gzhuoj-contest-service:regular_contest_problem_set:";

    public static final String TEAM_LOGIN_KEY = "gzhuoj-team:login:%s";

    // 比赛评测队列
    public static final String CONTEST_JUDGE_QUEUE = "gzhuoj-contest-judge:queue";
    // 非比赛评测队列
    public static final String COMMON_JUDGE_QUEUE = "gzhuoj-common-judge:queue";
    // 在线自测
    public static final String ONLINE_TEST_JUDGE_QUEUE = "gzhuoj-online-test-judge:queue";

}
