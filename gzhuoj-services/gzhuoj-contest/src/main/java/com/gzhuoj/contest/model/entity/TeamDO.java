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
@TableName(value = "team")
public class TeamDO {

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 队伍参赛情况 0 -> 参赛 1 -> 不参赛
     */
    private Integer teamStatus;

    /**
     * 密码
     */
    private String password;

    /**
     * 队伍名
     */
    private String teamName;

    /**
     * 队伍成员组成
     */
    private String teamMember;

    /**
     * “常规”（0）、“女队”（1）、“打星”（2）
     */
    private Integer teamType;

    /**
     * 教练
     */
    private String coach;

    /**
     * 学校
     */
    private String school;

    /**
     * 比赛场地
     */
    private String room;

    /**
     * 队伍权限
     */
    private Integer teamPrivilege;


}
