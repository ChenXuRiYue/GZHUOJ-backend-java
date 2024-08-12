package com.gzhuoj.contest.dto.req.contest;

import com.gzhuoj.contest.model.entity.ProblemMapDO;
import lombok.Data;

import java.util.List;

@Data
public class ContestUpdateReqDTO {

    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 新比赛编号
     */
    private Integer newContestId;

    /**
     * 比赛标题
     */
    private String title;

    /**
     * 开始年份
     */
    private String startYear;

    /**
     * 开始月份
     */
    private String startMonth;

    /**
     * 开始天
     */
    private String startDay;

    /**
     * 开始小时
     */
    private String startHour;

    /**
     * 开始分钟
     */
    private String startMinute;

    /**
     * 结束年份
     */
    private String endYear;

    /**
     * 结束月份
     */
    private String endMonth;

    /**
     * 结束天
     */
    private String endDay;

    /**
     * 结束小时
     */
    private String endHour;

    /**
     * 结束分钟
     */
    private String endMinute;

    /**
     * 比赛开放状态 0 -> 不开放， 1 -> 开放
     */
    private Integer contestStatus;

    /**
     * 比赛描述
     */
    private String description;

    /**
     * 比赛访问权限 0 -> private 1 -> public 2 -> protect(需要密码)
     */
    private Integer access;

    /**
     * 比赛可用语言
     */
    private List<Integer> language;

    /**
     * 比赛密码
     */
    private String password;

    /**
     * 每个学校或组织显示排名最高的k支队r
     */
    private Integer topteam;

    /**
     * 金奖获奖率
     */
    private String ratioGold;

    /**
     * 银奖获奖率
     */
    private String ratiosilver;

    /**
     * 铜奖获奖率
     */
    private String ratiobronze;


    /**
     * 封榜分钟数
     */
    private Integer frozenMinute;

    /**
     * 结束后持续封榜分钟数
     */
    private Integer frozenAfter;

    /**
     * 题目颜色映射
     */
    private List<ProblemMapDO> problemMapDOList;
}
