package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@TableName("submit")
public class SubmitDO {
    /**
     * 提交编号
     */
    private Integer submitId;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 题目编号
     */
    private Integer problemId;

    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 提交语言
     */
    private Integer language;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * 评测时间
     */
    private Integer execTime;

    /**
     * 评测结果
     */
    private Integer status;

    /**
     * 代码大小 -> byte
     */
    private Integer codeSize;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 评测机
     */
    private String judger;
}
