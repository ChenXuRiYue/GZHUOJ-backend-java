package com.gzhuoj.contest.dto.req.regContest;

import lombok.Data;

@Data
public class RegContestLoginReqDTO {

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 密码
     */
    private String password;
}
