package com.gzhuoj.contest.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.contest.model.entity.SubmitDO;
import lombok.Data;

@Data
public class RegContestStatusReqDTO extends Page<SubmitDO> {
    /**
     * 提交编号
     */
    private Integer submitId;

    /**
     * 题目编号
     */
    private Integer problemId;

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 提交语言
     */
    private Integer language;

    /**
     * 评测结果
     */
    private Integer status;
}
