package com.gzhuoj.contest.dto.resp.regContest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegContestLoginRespDTO {

    /**
     * 队伍编号
     */
    private String teamAccount;

    /**
     * 队伍名
     */
    private String teamName;
}
