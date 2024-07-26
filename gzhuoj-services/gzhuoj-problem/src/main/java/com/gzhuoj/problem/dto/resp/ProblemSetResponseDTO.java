package com.gzhuoj.problem.dto.resp;

import lombok.Data;

@Data
public class ProblemSetResponseDTO {
    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 题目标题
     */
    private String problemName;

    /**
     * author
     */
    private String author;

    /**
     * Accept Num
     */
    private Integer accepted;

    /**
     * submit NUm
     */
    private Integer submit;
}
