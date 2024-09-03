package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@TableName(value = "contest_description")
public class ContestDescDO {

    /**
     * 比赛编号
     */
    private Integer contestNum;

    /**
     * 比赛描述 markdown
     */
    private String description;

    /**
     * 比赛描述 Html
     */
    private String descriptionHtml;
}
