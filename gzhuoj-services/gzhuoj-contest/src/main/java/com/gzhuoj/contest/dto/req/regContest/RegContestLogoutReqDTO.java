package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestLogoutReqDTO {
    /**
     * 比赛编号
     */
    private Integer cid;

    /**
     * token
     */
    private String token;

}
