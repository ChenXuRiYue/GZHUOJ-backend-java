package com.gzhuoj.problem.service.Impl;

import com.gzhuoj.problem.dto.req.ListJudgeDataReqDTO;
import com.gzhuoj.problem.mapper.ProblemMapper;
import com.gzhuoj.problem.service.JudgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {
    private final ProblemMapper problemMapper;

    @Override
    public void judgeDataManager(ListJudgeDataReqDTO requestParam) {

    }

}
