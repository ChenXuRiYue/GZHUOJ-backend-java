package com.gzhuoj.problem.service.problem;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuacm.sdk.problem.model.dto.ProblemContentRespDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.problem.dto.req.problem.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.ListProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.UpdateProblemReqDTO;
import com.gzhuoj.problem.dto.resp.problem.ListProblemRespDTO;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.model.entity.ProblemDescrDO;

import java.util.List;

public interface ProblemService extends IService<ProblemDO> {
    void createProblem(CreateProblemReqDTO requestParam);

    IPage<ListProblemRespDTO> listProblem(ListProblemReqDTO requestParam);

    void updateProblem(UpdateProblemReqDTO requestParam);

    ProblemRespDTO queryProByNum(Integer num);

    ProblemDO selectProblemById(Integer id);

    ProblemDescrDO selectProblemDescrById(Integer problemNum);

    List<Object> selectTestExampleById(Integer problemNum);

    ProblemContentRespDTO getProblemContent(Integer problemNum);
}
