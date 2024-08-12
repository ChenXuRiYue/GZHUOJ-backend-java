package com.gzhuoj.contest.dto.req.contest;

import lombok.Data;

@Data
public class ContestStatusReqDTO {
    /**
     * 比赛的ID
     */
    private Integer id;

    /**
     * 当前状态
     */
    private Integer status;
}
