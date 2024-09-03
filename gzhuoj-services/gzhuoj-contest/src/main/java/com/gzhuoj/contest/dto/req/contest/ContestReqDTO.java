package com.gzhuoj.contest.dto.req.contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestReqDTO {

    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 比赛标题
     */
    private String title;

    /**
     * 用于开始时间的时间戳
     */
    private Long startTimes;

    /**
     * 用于结束时间的时间戳
     */
    private Long endTimes;

    /**
     * 比赛开放状态 0 -> 不开放， 1 -> 开放
     */
    private Integer contestStatus;

    /**
     * 比赛描述 markdown
     */
    private String description;

    /**
     * 比赛描述 Html
     */
    private String descriptionHtml;

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
     * 金牌个数
     */
    private Integer goldCount;
    /**
     * 银牌个数
     */
    private Integer silverCount;
    /**
     * 铜牌个数
     */
    private Integer bronzeCount;

    /**
     * 金奖获奖率
     */
    private String ratioGold;

    /**
     * 银奖获奖率
     */
    private String ratioSilver;

    /**
     * 铜奖获奖率
     */
    private String ratioBronze;


    /**
     * 封榜分钟数
     */
    private Integer frozenMinute;

    /**
     * 结束后持续封榜分钟数
     */
    private Integer frozenAfter;

    /**
     * 不好命名：
     */
    private List<SelectedProblemMsgWhenCreateContest> selectedProblemMsgWhenCreateContestList;

}
