package com.gzhuoj.contest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestDTO {
    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 比赛标题
     */
    private String title;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 比赛开放状态 0 -> 不开放， 1 -> 开放
     */
    private Integer contestStatus;

    /**
     * 比赛访问权限 0 -> private 1 -> public 2 -> protect(需要密码)
     */
    private Integer access;

    /**
     * 比赛可用语言的二进制编码rn0 -> c，rn1 -> c，rn2 -> java，rn3 -> python，rn4 -> go
     */
    private Integer languageMask;

    /**
     * 比赛密码
     */
    private String password;

    /**
     * 比赛描述附件
     */
    private String attach;


    /**
     * 获奖比例
     */
    private String awardRatio;

    /**
     * 奖牌数量分配
     */
    private String medalAssign;

    /**
     * 封榜分钟数
     */
    private Integer frozenMinute;

    /**
     * 结束后持续封榜分钟数
     */
    private Integer frozenAfter;
}
