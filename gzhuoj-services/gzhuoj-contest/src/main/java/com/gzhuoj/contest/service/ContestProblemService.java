package com.gzhuoj.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.model.entity.ContestProblemDO;

import java.util.List;

public interface ContestProblemService extends IService<ContestProblemDO> {
    List<ContestProblemDO> getAllProblem(Integer cid);
}
