package com.gzhuoj.contest.dto.req.regContest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.contest.model.entity.SubmitDO;
import lombok.Data;

@Data
public class RegContestStatusReqDTO extends Page<SubmitDO> {

    /**
     * 题目编号
     */
    private Integer problemId;

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
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 评测结果
     */
    private Integer status;
}
