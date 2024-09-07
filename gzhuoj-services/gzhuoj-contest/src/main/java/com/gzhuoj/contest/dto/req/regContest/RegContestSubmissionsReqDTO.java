package com.gzhuoj.contest.dto.req.regContest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.contest.model.entity.SubmitDO;
import lombok.Data;

import java.util.Date;

@Data
public class RegContestSubmissionsReqDTO extends Page<SubmitDO> {

    /**
     * 比赛编号
     */
    private Integer contestNum;
    /**
     * 赛中题目编号
     */
    private Integer problemLetterIndex;

    /**
     * 题目集中题目编号
     */
    private Integer problemNum;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 队伍名
     */
    private String teamName;

    /**
     * 学校
     */
    private String school;


    /**
     * 评测结果
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
