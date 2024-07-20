package com.gzhuoj.usr.remote.dto.req;

import lombok.Data;

@Data
public class UpdateProblemReqDTO {
    /**
     * 题目标题
     */
    private String problemName;

    /**
     * 题目题面
     */
    private String description;

    /**
     * 题目参考代码
     */
    private String solution;

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

    /**
     * 新录题序号
     */
    private Integer newProblemNum;

    /**
     * 题目类型
     */
    private Integer ProblemType;

    /**
     * 题目状态
     */
    private Integer problemStatus;
}
