package com.gzhuoj.contest.service.contestProblem;

import com.baomidou.mybatisplus.extension.service.IService;

import com.gzhuacm.sdk.problem.model.dto.ProblemContentRespDTO;
import com.gzhuoj.contest.dto.req.contestProblem.ContestResultReqDTO;
import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.pojo.ContestProblemCalculation;


import java.util.List;

public interface ContestProblemService extends IService<ContestProblemDO> {
    List<ContestProblemDO> getAllProblem(Integer contestNum);

    List<ContestProblemCalculation> getContestProblemsResult(ContestResultReqDTO contestResultReqDTO);

    ContestResultRespDTO getResult(Integer ContestNum);

    ProblemContentRespDTO getContestProblem(Integer contestNum, Integer problemNumInContest);

    List<ContestProblemCalculation> getContestProblemCalculation(ContestResultReqDTO contestResultReqDTO);
}
