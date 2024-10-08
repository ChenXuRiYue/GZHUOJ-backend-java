package com.gzhuoj.usr.dto.resp.privilege;

import lombok.Data;

@Data
public class AdminPrivilegeListRespDTO {
    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户角色
     */
    private String role;
}
