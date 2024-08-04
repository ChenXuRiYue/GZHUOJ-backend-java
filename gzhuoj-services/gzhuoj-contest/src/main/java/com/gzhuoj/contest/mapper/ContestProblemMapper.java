package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.contest.dto.resp.RegContestProSetRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.pojo.SFC;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ContestProblemMapper extends BaseMapper<ContestProblemDO> {
    /**
     * 输入查询比赛题目汇总信息条件
     * @return  输出查询数值
     *
     */
    Integer selectForContest(SFC sfc);

    @Select("select * from contest_problem where problem_id=#{problemId} and contest_id=#{contestId}")
    ContestProblemDO selectByProblemId(@Param("problemId") Integer problemId,@Param("contestId")Integer contestId);

    @Select("select * from contest_problem where contest_id=#{contestId} order by actual_num asc")
    List<ContestProblemDO> selectByContestId(Integer contestId);
}
