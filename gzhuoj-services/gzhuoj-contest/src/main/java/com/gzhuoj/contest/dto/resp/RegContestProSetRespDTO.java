package com.gzhuoj.contest.dto.resp;

import com.alibaba.fastjson2.annotation.JSONField;
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
public class RegContestProSetRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 录题序号
     */
    @JSONField(name = "problem_num")
    private Integer problemNum;

    /**
     * 题目标题
     */
    @JSONField(name = "problem_name")
    private String problemName;

    /**
     * 题目气球颜色
     */
    @JSONField(name = "color")
    private String color;

    /**
     * AC总数
     */
    @JSONField(name = "accepted")
    private Integer accepted;

    /**
     * 时间限制
     */
    @JSONField(name = "time_limit")
    private Integer timeLimit;

    /**
     * 空间限制
     */
    @JSONField(name = "memory_limit")
    private Integer memoryLimit;

    /**
     * 题目在比赛中的编号
     */
    @JSONField(name = "actual_num")
    private Integer actualNum;

    /**
     * 自己team是否AC
     */
    @JSONField(name = "ac")
    private boolean AC;
}
