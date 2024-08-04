package com.gzhuoj.contest.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RegContestStatusRespDTO {

    /**
     * 提交编号
     */
    private Integer submitId;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 题目编号
     */
    private Integer problemId;

    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 提交语言
     */
    private Integer language;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * 评测时间
     */
    private Integer execTime;

    /**
     * 评测结果
     */
    private Integer status;

    /**
     * 代码大小 -> byte
     */
    private Integer codeSize;

    /**
     * 提交时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;
}
