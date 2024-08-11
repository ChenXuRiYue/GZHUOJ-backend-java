package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@TableName(value = "problem_description")
public class ProblemDescrDO {
    /**
     * 录题序号
     */
    private Integer problemNum;

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

}
