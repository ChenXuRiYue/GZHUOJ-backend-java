package com.gzhuoj.usr.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 用户角色
     */
    private Integer role;
}
