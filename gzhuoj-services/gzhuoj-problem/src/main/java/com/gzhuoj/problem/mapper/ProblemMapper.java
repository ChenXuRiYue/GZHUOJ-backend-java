package com.gzhuoj.problem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.problem.dto.req.CreateProblemReqDTO;
import com.gzhuoj.problem.model.entity.ProblemDO;
import org.apache.ibatis.annotations.Mapper;


public interface ProblemMapper extends BaseMapper<ProblemDO> {
//    void createProblem(CreateProblemReqDTO createProblemReqDTO);
}
