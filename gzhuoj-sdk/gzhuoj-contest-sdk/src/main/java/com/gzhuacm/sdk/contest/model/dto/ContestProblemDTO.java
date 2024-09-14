package com.gzhuacm.sdk.contest.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestProblemDTO {
    private Integer contestNum;
    private Integer problemNum;
    private Integer problemLetterIndex;
}
