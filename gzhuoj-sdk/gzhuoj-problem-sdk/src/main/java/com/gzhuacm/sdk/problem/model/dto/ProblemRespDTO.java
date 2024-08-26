package com.gzhuacm.sdk.problem.model.dto;


import lombok.Data;

@Data
public class ProblemRespDTO {
    /**
     * 题目标题
     */
    private String problemName;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;

    /**
     * Accept Num
     */
    private Integer accepted;

    /**
     * solved Num
     */
    private Integer solved;

    /**
     * submit NUm
     */
    private Integer submit;

    /**
     * author
     */
    private String author;

    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 题目类型
     */
    private Integer ProblemType;

    /**
     * 题目状态
     */
    private Integer problemStatus;

    /**
     * 是否启用spj 0 -> 不启用， 1 -> 启用
     */
    private Integer spj;

    /**
     * 附件文件夹唯一标识
     * 格式：yyyy-MM-dd_16位随机字符串
     */
    private String attach;
}
