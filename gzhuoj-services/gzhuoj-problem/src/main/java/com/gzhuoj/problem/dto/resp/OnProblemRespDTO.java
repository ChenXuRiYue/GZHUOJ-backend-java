package com.gzhuoj.problem.dto.resp;

import lombok.Data;

@Data
public class OnProblemRespDTO {
    /**
     * 题目标题
     */
    private String problemName;

    /**
     * 题目题面
     */
    private String description;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;

    /**
     * 录题序号
     */
    private Integer problemNum;
}
