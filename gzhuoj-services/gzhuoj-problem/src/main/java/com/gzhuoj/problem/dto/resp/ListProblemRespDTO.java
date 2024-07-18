package com.gzhuoj.problem.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class ListProblemRespDTO {
    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 题目标题
     */
    private String problemName;

    /**
     * 题目状态
     */
    private Integer problemStatus;

    /**
     * 创建时间
     */
    private Date createTime;
}
