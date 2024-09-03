package com.gzhuacm.sdk.problem.model.dto;

import lombok.Data;

@Data
public class ProblemJudgeResourcesRespDTO {
    /**
     * 题库内题目序号
     */
    private Integer problemNum;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件内容
     */
    private String fileContent;
}
