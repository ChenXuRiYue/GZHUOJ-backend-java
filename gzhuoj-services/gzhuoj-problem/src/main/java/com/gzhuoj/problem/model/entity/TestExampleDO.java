package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import common.database.Base.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@TableName(value = "test_example")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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

    /**
     * 题目编号
     */
    private Integer problemId;
}
