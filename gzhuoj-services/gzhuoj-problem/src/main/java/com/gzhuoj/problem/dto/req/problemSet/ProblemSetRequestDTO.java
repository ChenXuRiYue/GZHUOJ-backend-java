package com.gzhuoj.problem.dto.req.problemSet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.problem.model.entity.ProblemDO;
import lombok.Data;

@Data
public class ProblemSetRequestDTO extends Page<ProblemDO> {
    /**
     * 关键字
     */
    private String search;

    /**
     * 排序
     */
    private String order;


}
