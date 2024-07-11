package com.gzhuoj.problem.model.entity;

import lombok.Data;

@Data
public class TestCase {
    private String name;

    /**
     * 题目题面
     */
    private String description;

    /**
     * 题目参考代码
     */
    private String soluction;
}
