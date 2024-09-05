package com.gzhuoj.contest.model.pojo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类用于接受查询比赛中某题的汇总结果
 * 1. 语言 ——> 提交
 */

@Data
@RequiredArgsConstructor
public class ContestProblemCalculation {
    // 这个字段，对于前端而言，在于显示细节时候推断。仅用于视图层的结构中，直接转成可视的字符串。
    private String problemLetter;
    private Map<String, Integer> languageCalculation;
    // status Id -> total
    private Map<String, Integer> statusCalculation;
    private Integer total;
    private Integer acTotal;

    public void preventNullPointer(){
        if(ObjectUtils.isEmpty(languageCalculation)){
            this.languageCalculation = new HashMap<>();
        }
        if(ObjectUtils.isEmpty(statusCalculation)){
            this.statusCalculation = new HashMap<>();
        }
    }
}
