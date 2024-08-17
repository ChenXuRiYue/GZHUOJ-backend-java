package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestUpdateTeamReqDTO {

    /**
     * 比赛id
     */
    private Integer cid;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 队伍名
     */
    private String teamName;

    /**
     * 队伍成员组成
     */
    private String teamMember;

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
     * “常规”（0）、“女队”（1）、“打星”（2）
     */
    private Integer teamType;

    /**
     * 账号权限
     */
    private Integer teamPrivilege;

    /**
     * 密码
     */
    private String newPassword;
}