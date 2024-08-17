package com.gzhuoj.problem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.model.entity.ProblemDescrDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ProblemMapper extends BaseMapper<ProblemDO> {
//    void createProblem(CreateProblemReqDTO createProblemReqDTO);
    @Select("SELECT * from `gzhuoj-problem`.problem where id=#{problemId}")
    public ProblemDO selectProblemById(Integer problemId);

    @Select("SELECT * from `gzhuoj-problem`.problem_description where id=#{problemId}")
    public ProblemDescrDO selectProblemDesceById(Integer problemId);

    @Select("SELECT * from `gzhuoj-problem`.test_example where test_example.problem_id=#{problemId}")
    List<TestExampleDO> selectTestExampleById(Integer problemId);
}
