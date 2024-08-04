package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@TableName(value = "contest_problem")
public class ContestProblemDO {

    private Integer id;
    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 题目集中的编号
     */
    private Integer problemId;

    /**
     * 题目对应气球颜色rgb十六进制编号
     */
    private String problemColor;

    /**
     * 题目在比赛中的编号
     */
    private Integer actualNum;
}
