package com.gzhuoj.contest.model.example;

import lombok.Data;

import java.util.Date;
/**
 * 该类用于查询比赛中某一题的汇总结果
 */
@Data
public class ContestProblemSubmissionsCalculateExample {
    private Integer contestNum;
    private Integer problemNum;
    private Date startTime;
    private Date endTime;
    private Integer language;
    private Integer status;
}
