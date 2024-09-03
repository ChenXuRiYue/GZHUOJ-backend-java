package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestGenTeamReqDTO {
    /**
     * 标准比赛编号
     */
    private Integer contestNum;

    /**
     * 生成的队伍是否覆盖当前所有
     */
    private String reset;

    /**
     * 队伍描述
     */
    private String description;
}
