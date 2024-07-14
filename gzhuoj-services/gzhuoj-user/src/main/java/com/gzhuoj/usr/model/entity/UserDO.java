package com.gzhuoj.usr.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("user")
public class UserDO extends BaseDO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户组织
     */
    private String orgnization;

    /**
     * 用户角色
     */
    private Integer role;

    /**
     * 用户昵称
     */
    private String nickname;
}
