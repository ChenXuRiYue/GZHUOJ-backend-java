package com.gzhuoj.contest.dto.resp;

import lombok.Data;

@Data
public class RegContestProSetRespDTO {

    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 题目标题
     */
    private String problemName;

    /**
     * 题目气球颜色
     */
    private String color;

    /**
     * AC总数
     */
    private Integer accepted;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;
}
