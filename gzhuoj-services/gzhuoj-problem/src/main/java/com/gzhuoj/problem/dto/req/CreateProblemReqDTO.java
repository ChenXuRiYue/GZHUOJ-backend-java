package com.gzhuoj.problem.dto.req;

import com.gzhuoj.problem.model.entity.TestCaseDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import lombok.Data;

import java.util.List;

@Data
public class CreateProblemReqDTO {

    /**
     * 录题序号
     */
    private Integer problemNum;

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
     *题目类型： 0 -> 普通题目， 1 -> special judge, 2 -> 交互题
     */
    private Integer problemType;

    /**
     * 题目题面
     */
    private String description;

    /**
     * 题目体面Html
     */
    private String descriptionHtml;

    /**
     * 输入描述
     */
    private String inputDescription;

    /**
     * 输入描述Html
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
     * 样例描述
     */
    private String explanation;

    /**
     * 样例描述Html
     */
    private String explanationHtml;

    /**
     * author
     */
    private String author;

    /**
     * 是否启用spj 0 -> 不启用， 1 -> 启用
     */
    private Integer spj;

    /**
     * 样例
     */
    List<TestExampleDO> testExampleList;
}

