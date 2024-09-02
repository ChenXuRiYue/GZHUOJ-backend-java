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
@TableName("problem_judge_resources")
public class ProblemJudgeResourcesDO extends BaseDO {
    /**
     * 题库内题目序号
     */
    private Integer problemId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件内容
     */
    private String fileContent;
}
