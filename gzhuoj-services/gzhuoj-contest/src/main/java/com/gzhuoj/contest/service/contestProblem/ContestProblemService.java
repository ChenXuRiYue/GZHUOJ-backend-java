package com.gzhuoj.contest.service.contestProblem;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.pojo.CPResult;


import java.util.Date;
import java.util.List;

public interface ContestProblemService extends IService<ContestProblemDO> {
    List<ContestProblemDO> getAllProblem(Integer cid);

    CPResult getProblemResult(Integer contestId, Integer problemId, Date beginTime, Date endTime);

    ContestResultRespDTO getResult(Integer ContestId);
}
