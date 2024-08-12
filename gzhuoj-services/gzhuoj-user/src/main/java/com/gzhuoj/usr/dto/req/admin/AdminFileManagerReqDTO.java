package com.gzhuoj.usr.dto.req.admin;

import lombok.Data;

@Data
public class AdminFileManagerReqDTO {
    /**
     * 查询的文件的类别： 题目，比赛
     */
    private String item;

    /**
     * 文件的ID
     */
    private Integer id;

    /**
     * 查询关键字
     */
    private String search;
}
