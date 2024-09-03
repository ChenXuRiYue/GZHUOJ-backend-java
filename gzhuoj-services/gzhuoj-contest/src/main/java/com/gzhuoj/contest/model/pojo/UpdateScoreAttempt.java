package com.gzhuoj.contest.model.pojo;

import common.enums.SubmissionStatus;
import lombok.Data;

@Data
public class UpdateScoreAttempt {
    private String contestNum;
    private CompetitorBasicInfo competitor;
    private Integer passTime ;
    private Integer punishTime;
    private String ProblemNum;
    private SubmissionStatus submissionStatus;
}
