package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@TableName("contest_balloon")
public class ContestBalloonDO {
    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 题目编号
     */
    private Integer problemNum;

    /**
     * 队伍账号
     */
    private String teamAccount;

    /**
     * 比赛室号
     */
    private String room;

    /**
     * 距离比赛开始的时间
     * HH:MM:SS
     */
    private Integer acTime;

    /**
     * problem status，2 ac、3 fb
     */
    private Integer pst;

    /**
     * balloon status， 4分配，5已发
     */
    private Integer bst;

    /**
     * 气球配分员
     */
    private String balloonSender;
}
