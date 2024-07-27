package com.gzhuoj.contest.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class ContestAllRespDTO {
    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 比赛标题
     */
    private String title;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 比赛开放状态 0 -> 不开放， 1 -> 开放
     */
    private Integer contestStatus;
}
