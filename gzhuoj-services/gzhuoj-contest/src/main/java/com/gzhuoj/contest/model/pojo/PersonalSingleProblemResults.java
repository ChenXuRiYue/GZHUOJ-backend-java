package com.gzhuoj.contest.model.pojo;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PersonalSingleProblemResults {
    // 题目 -> A B C...
    private String problemNum;

    // 表示是否已经通过该题。
    private Boolean status;

    // 首次通过的时间
    private String timeStr;

    // 首次通过时间- 时间戳
    private Long times;

    // 有效罚时提交次数。
    private Integer penaltyCount;

    PersonalSingleProblemResults(String problemNum){
        this.problemNum = problemNum;
        this.status = false;
        this.penaltyCount = 0;
    }

    // 计算时间差有点麻烦 TODO
    public void pass(){
        if (Boolean.TRUE.equals(this.status)) return;
        status = true;
        times = System.currentTimeMillis();
    }
    // TODO 测试的时候要处理时间
    public void reject(){
        if(Boolean.TRUE.equals(this.status)) return;
        penaltyCount++;
    }
}
