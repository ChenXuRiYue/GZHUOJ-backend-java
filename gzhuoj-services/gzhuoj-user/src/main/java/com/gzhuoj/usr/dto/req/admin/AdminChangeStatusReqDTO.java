package com.gzhuoj.usr.dto.req.admin;

import lombok.Data;

@Data
public class AdminChangeStatusReqDTO {
    /**
     * 可以修改的事件状态： 题目，比赛
     */
    private String item;

    /**
     * 事件的ID
     */
    private Integer id;

    /**
     * 当前状态
     */
    private Integer status;
}
