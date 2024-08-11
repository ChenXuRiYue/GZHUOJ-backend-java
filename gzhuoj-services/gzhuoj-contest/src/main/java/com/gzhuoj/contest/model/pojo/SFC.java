package com.gzhuoj.contest.model.pojo;

import java.util.Date;
import java.sql.Time;
/**
 * 该类用于查询比赛中某一题的汇总结果
 */
public class SFC {
    public Integer contestId;
    public Integer problemId;
    public Date beginTime;
    public Date endTime;
    public Integer language;
    public Integer status;
}
