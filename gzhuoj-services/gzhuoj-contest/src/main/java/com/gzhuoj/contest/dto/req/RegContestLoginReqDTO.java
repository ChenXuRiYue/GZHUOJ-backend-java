package com.gzhuoj.contest.dto.req;

import lombok.Data;

@Data
public class RegContestLoginReqDTO {
    /**
     * 队伍编号
     */
    private String teamId;

    /**
     * 密码
     */
    private String password;
}
