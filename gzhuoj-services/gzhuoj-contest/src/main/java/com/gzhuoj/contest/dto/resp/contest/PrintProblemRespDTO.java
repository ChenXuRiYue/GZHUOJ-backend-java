package com.gzhuoj.contest.dto.resp.contest;

import com.gzhuoj.contest.model.entity.ContestDO;
import common.model.pojo.ProblemPrintDTO;
import lombok.Data;

import java.util.List;


@Data
public class PrintProblemRespDTO {
    ContestDO contest;
    List<ProblemPrintDTO> problems;
}
