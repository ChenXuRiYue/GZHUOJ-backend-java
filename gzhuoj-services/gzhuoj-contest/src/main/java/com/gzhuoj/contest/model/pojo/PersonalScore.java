package com.gzhuoj.contest.model.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Map;


// 排行榜对外展示的数据。
@Data
@Builder
public class PersonalScore {
    // 个人基本信息
    private CompetitorBasicInfo competitor;
    // 排名
    private Integer standing;
    // 已通过题目编号，以及该通过题目的首次通过时间。 精确到秒级 （事实上，数据库是精确到毫秒级的，但是对外开发的视图中没有必要精确到秒。）
    private Map<String , PersonalSingleProblemResults> problemResultsDetails;
    // 一些重要量的统计
    private String dirt;
    // passed
    private Integer passed;
    // 罚时 总数
    private Long penalty;
}
