package com.gzhuoj.usr.dto.req;

import lombok.Data;

@Data
public class UserInfoUpdateReqDTO {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户组织
     */
    private String organization;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     * 用于验证
     */
    private String password;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 再次验证新密码
     */
    private String verifyNewPassword;
}
