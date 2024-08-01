package com.gzhuoj.contest.dto.req;

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
