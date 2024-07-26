package com.gzhuoj.problem.dto.req;

import com.gzhuoj.problem.model.entity.TestCaseDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import lombok.Data;

import java.util.List;

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
