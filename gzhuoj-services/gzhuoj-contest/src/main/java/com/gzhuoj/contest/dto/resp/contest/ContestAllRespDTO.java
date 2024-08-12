package com.gzhuoj.contest.dto.resp.contest;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 比赛开放状态 0 -> 不开放， 1 -> 开放
     */
    private Integer contestStatus;
}
