package com.gzhuoj.problem.dto.req;

import com.gzhuoj.problem.model.entity.TestCaseDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
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
     * 题面Html
     */
    private String descriptionHtml;

    /**
     * 输入描述
     */
    private String inputDescrition;

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
     * 样例解释Html
     */
    private String explanationHtml;

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
     * 是否启用spj 0 -> 不启用， 1 -> 启用
     */
    private Integer spj;

    /**
     * 题目状态
     */
    private Integer problemStatus;

    /**
     * author
     */
    private String author;

    /**
     * 题目类型
     */
    private Integer ProblemType;

    /**
     * 样例
     */
    List<TestExampleDO> testExampleList;
}
