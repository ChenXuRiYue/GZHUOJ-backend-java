package com.gzhuoj.contest.dto.req;

import lombok.Data;

@Data
public class ContestBalloonQueueReqDTO {
    /**
     * 比赛编号
     */
    private Integer cid;

    /**
     * 比赛室号
     */
    private String room;
}
