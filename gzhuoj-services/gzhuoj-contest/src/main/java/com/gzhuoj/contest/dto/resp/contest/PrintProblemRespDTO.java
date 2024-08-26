package com.gzhuoj.contest.dto.resp.contest;

import com.gzhuacm.sdk.problem.model.dto.ProblemPrintDTO;
import com.gzhuoj.contest.model.entity.ContestDO;
import lombok.Data;

import java.util.List;


@Data
public class PrintProblemRespDTO {
    ContestDO contest;
    List<ProblemPrintDTO> problems;
}
