package com.gzhuoj.problem.service.judge;

import com.gzhuoj.problem.dto.req.judge.ListJudgeDataReqDTO;


public interface JudgeService {
    void judgeDataManager(ListJudgeDataReqDTO requestParam);
}
