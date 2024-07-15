package com.gzhuoj.usr.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoRespDTO {
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户角色
     */
    private Integer role;
}
