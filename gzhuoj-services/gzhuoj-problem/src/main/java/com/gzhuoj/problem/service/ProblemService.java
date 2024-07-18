package com.gzhuoj.problem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.problem.dto.req.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.ListProblemReqDTO;
import com.gzhuoj.problem.dto.resp.ListProblemRespDTO;
import com.gzhuoj.problem.model.entity.ProblemDO;

public interface ProblemService extends IService<ProblemDO> {
    void createProblem(CreateProblemReqDTO createProblemReqDTO);

    IPage<ListProblemRespDTO> listProblem(ListProblemReqDTO requestParam);
}
