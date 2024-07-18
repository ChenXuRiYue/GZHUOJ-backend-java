package com.gzhuoj.problem.model.entity;

import common.database.BaseDO;
import lombok.Data;

@Data
public class TestCaseDO extends BaseDO {
    /**
     * 数据在对应数据集中的相对序号
     */
    private Integer testCaseNum;

    /**
     * 输入
     */
    private String input;

    /**
     * 输出
     */
    private String output;
}
