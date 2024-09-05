package com.gzhuoj.contest.dto.req.contestProblem;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestResultReqDTO {
    private Integer contestNum;
    private Integer languageMask;
    // 如果传入为空，此时也默认全部处理
    private List<Integer> problemLetterIndexes;
//    用于处理封榜的情况
    private Long startTimes;
    private Long endTimes;
}
