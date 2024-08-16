package com.gzhuoj.contest.dto.resp.contest;

import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.pojo.ProblemPrint;
import lombok.Data;

import java.util.List;


@Data
public class PrintProblemRespDTO {
    ContestDO contest;
    List<ProblemPrint> problems;
}
