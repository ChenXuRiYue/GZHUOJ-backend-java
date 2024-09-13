package com.gzhuoj.contest.dto.req.contest;

import lombok.Data;

@Data
public class ContestUpdateInfoPushReqDTO {
    /**
     * 比赛Id
     */
    private Integer contestId;

    /**
     * 推送的消息
     */
    private String message;
}
