package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import common.database.Base.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@TableName(value = "test_case")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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

    /**
     * 题目编号
     */
    private Integer problemId;
}
