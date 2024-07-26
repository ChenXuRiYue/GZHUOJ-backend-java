package com.gzhuoj.problem.dto.req;

import lombok.Data;

@Data
public class ProblemSetRequestDTO {
    /**
     * 关键字
     */
    private String search;

    /**
     * 排序
     */
    private String order;


}
