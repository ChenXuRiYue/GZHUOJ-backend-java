package com.gzhuoj.contest.dto.resp.regContest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RegContestProblemRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 录题序号
     */
    private Integer problemNum;

    /**
     * 题目标题
     */
    private String problemName;


    /**
     * AC总数
     */
    private Integer accepted;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;

    /**
     * 题目在比赛中的编号
     */
    private Integer problemLetterIndex;

    /**
     * 自己team是否AC
     * TODO 未尝试， 尝试未通过， 通过
     */
    private boolean AC;
}
