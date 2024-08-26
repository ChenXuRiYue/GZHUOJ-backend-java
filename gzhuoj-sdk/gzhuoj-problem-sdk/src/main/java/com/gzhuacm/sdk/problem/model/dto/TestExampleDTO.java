package com.gzhuacm.sdk.problem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestExampleDTO {
    /**
     * 题目样例集中的序号
     */
    private Integer testExampleNum;

    /**
     * 输入
     */
    private String input;

    /**
     * 输出
     */
    private String output;

    /**
     * 题目编号
     */
    private Integer problemNum;
}
