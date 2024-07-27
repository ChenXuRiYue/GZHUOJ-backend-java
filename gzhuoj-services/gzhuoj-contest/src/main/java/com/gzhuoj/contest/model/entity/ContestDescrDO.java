package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@TableName(value = "contest_description")
public class ContestDescrDO {

    /**
     * 比赛编号
     */
    private Integer contestId;

    /**
     * 比赛描述
     */
    private String description;
}
