package com.gzhuoj.usr.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserLoginRespDTO {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;
}
