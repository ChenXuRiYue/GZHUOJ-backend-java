package com.gzhuoj.contest.dto.req.contest;

import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.contest.model.entity.ContestDO;
import lombok.Data;


@Data
public class ContestAllReqDTO extends Page<ContestDO>{
    /**
     * 关键字
     */
    private String search;

    /**
     * 查询类型 0. 公共视图， 管理员视图。
     */
    private Integer type;

    private Pair<String, String> order;
}
