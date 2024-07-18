package com.gzhuoj.problem.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.problem.model.entity.ProblemDO;
import lombok.Data;

@Data
public class ListProblemReqDTO extends Page<ProblemDO> {
    /**
     * 关键字
     */
    private String search;

    /**
     * 排序方式 -> 顺序 / 倒序
     */
    private String order;
}
