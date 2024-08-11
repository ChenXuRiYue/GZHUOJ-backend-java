package com.gzhuoj.contest.dto.req.Judge;

import lombok.Data;

@Data
public class RegContestJudgeSubmitReqDTO {

    /**
     * 比赛id
     */
    private Integer cid;

    /**
     * 题目编号
     */
    private Integer problemId;

    /**
     * 提交语言
     */
    private Integer language;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 队伍账号
     */
    private String teamAccount;
}
