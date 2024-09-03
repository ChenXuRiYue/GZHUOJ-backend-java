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
    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 题目集中的编号
     */
    private Integer problemId;

    /**
     * 题目在比赛中的编号
     */
    private Integer actualNum;
}
