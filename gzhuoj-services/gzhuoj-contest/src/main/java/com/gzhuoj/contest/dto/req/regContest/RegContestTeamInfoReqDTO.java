package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestTeamInfoReqDTO {
    
    /**
     * 比赛id
     */
    private Integer contestNum;

    /**
     * 队伍编号
     */
    private String teamAccount;
}
