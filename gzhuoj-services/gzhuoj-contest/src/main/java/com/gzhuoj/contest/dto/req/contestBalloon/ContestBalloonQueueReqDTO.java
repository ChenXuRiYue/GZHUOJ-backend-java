package com.gzhuoj.contest.dto.req.contestBalloon;

import lombok.Data;

@Data
public class ContestBalloonQueueReqDTO {
    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 比赛室号
     */
    private String room;
}
