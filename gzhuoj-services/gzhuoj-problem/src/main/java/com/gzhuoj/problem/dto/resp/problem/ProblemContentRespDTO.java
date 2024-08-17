package com.gzhuoj.problem.dto.resp.problem;


import com.gzhuoj.problem.model.entity.TestExampleDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ProblemContentRespDTO {
    /**
     * 题目标题
     */
    private String problemName;


    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * author
     */
    private String author;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目描述HTML
     */
    private String descriptionHtml;

    /**
     * 输入描述
     */
    private String inputDescription;

    /**
     * 输入描述HTML
     */
    private String inputDescriptionHtml;

    /**
     * 输出描述
     */
    private String outputDescription;

    /**
     * 输出描述Html
     */
    private String outputDescriptionHtml;


    /**
     * 样例解释
     */
    private String explanation;

    /**
     * 样例解释 Html
     */
    private String explanationHtml;

    /**
     * 题目样例
     */
    private List<TestExampleDO> testExamples;

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
    // TODO problemType 和 spj 冲突
    private Integer spj;
}
