package com.gzhuoj.usr.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


/**
 * 用户表实体
 */

@Builder
@Data
public class UserDO {
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标记账号是否已经被删除 0 -> 未删除， 1 -》已删除
     */
    private Integer deleteflag;
}
