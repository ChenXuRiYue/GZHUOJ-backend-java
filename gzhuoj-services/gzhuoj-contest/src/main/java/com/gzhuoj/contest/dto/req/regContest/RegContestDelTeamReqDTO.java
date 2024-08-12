package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestDelTeamReqDTO {
    /**
     * 比赛id
     */
    private Integer cid;

    /**
     * 队伍id
     */
    private String teamAccount;
}
