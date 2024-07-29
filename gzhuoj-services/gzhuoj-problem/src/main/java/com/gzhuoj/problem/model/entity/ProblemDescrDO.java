package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName(value = "problem_description")
public class ProblemDescrDO {

    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 比赛描述
     */
    private String description;
}
