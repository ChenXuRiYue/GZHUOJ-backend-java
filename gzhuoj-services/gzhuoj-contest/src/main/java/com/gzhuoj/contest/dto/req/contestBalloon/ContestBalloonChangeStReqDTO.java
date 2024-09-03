package com.gzhuoj.contest.dto.req.contestBalloon;

import lombok.Data;

@Data
public class ContestBalloonChangeStReqDTO {
    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 题目编号
     */
    private Integer problemNum;

    /**
     * balloon status， 0未发、1已发
     */
    private Integer bst;
}
