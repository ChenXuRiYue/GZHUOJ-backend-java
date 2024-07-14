package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import common.database.BaseDO;
import lombok.Data;

import java.sql.Date;


@Data
public class Problem extends BaseDO {
    /**
     * 题目标题
     */
    private String problemName;

    /**
     * 题目题面
     */
    private String description;

    /**
     * 题目参考代码
     */
    private String solution;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;


    /**
     * 出题人
     */
    private Integer author;

    /**
     * 录题序号
     */
    private Integer problemNum;
}
