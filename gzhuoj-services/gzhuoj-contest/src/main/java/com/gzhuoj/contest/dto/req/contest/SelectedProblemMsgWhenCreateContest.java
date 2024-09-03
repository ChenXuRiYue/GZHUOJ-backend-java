package com.gzhuoj.contest.dto.req.contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedProblemMsgWhenCreateContest {
    private String  sourceOj;
    private Integer problemNum;
    private String  problemAddress;
    private String  problemTitleInOj;
    private String  problemTitleInContest;
}
