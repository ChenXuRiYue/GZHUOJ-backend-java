package com.gzhuoj.problem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import org.apache.ibatis.annotations.Select;


public interface ProblemMapper extends BaseMapper<ProblemDO> {
//    void createProblem(CreateProblemReqDTO createProblemReqDTO);
    @Select("SELECT * from `gzhuoj-problem`.problem where id=#{problemId}")
    public ProblemDO selectProblemById(Integer problemId);
}
