package com.gzhuoj.usr.dto.req;

import lombok.Data;

@Data
public class
UserLoginReqDTO {
    /**
     *  用户账号
     */
    private String userAccount;

    /**
     *  用户密码
     */
    private String password;
}
