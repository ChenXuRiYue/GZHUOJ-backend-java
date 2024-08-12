package com.gzhuoj.contest.dto.req.contest;

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
     * 排序方式 -> 顺序 / 倒序
     */
    private String order;
}
