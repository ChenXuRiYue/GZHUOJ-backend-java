package com.gzhuoj.contest.dto.req;

import lombok.Data;

@Data
public class RegContestTeamInfoReqDTO {
    
    /**
     * 比赛id
     */
    private Integer cid;

    /**
     * 队伍编号
     */
    private String teamId;
}
