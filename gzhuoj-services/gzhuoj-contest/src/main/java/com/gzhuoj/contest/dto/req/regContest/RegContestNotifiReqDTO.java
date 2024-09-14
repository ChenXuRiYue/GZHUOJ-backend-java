package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestNotifiReqDTO {
    /**
     * 标准比赛编号
     */
    private Integer contestNum;

    /**
     * 消息
     */
    private String message;
}
