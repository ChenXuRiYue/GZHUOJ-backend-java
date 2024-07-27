package com.gzhuoj.usr.remote.entity;

import lombok.Data;

/**
 * 比赛题目编号与气球颜色对应实体
 */
@Data
public class ProblemMapDO {
    /**
     * 题目编号
     */
    private Integer problemNum;

    /**
     * 题目气球颜色
     */
    private String color;
}