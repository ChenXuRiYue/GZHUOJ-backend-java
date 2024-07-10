package com.gzhuoj.problem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class Problem {

    /**
     * 题目标题
     */
    private String name;

    /**
     * 题目题面
     */
    private String description;

    /**
     * 题目参考代码
     */
    private String soluction;
}
