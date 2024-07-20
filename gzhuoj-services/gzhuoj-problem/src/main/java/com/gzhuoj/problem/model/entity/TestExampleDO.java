package com.gzhuoj.problem.model.entity;

import common.database.Base.BaseDO;

public class TestExampleDO extends BaseDO {
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
     * 样例说明
     */
    private String explanation;
}
