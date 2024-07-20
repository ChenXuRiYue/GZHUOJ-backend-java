package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import common.database.Base.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("problem")
public class ProblemDO extends BaseDO {
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
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 题目类型
     */
    private Integer ProblemType;

    /**
     * 题目状态
     */
    private Integer problemStatus;
}
