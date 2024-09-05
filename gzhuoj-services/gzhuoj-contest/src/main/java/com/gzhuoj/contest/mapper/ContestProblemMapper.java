package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuacm.sdk.contest.model.dto.ContestProblemDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.example.ContestProblemSubmissionsCalculateExample;
import com.gzhuoj.contest.model.pojo.ContestProblemCalculation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestProblemMapper extends BaseMapper<ContestProblemDO> {
    /**
     * 输入查询比赛题目汇总信息条件
     * @return  输出查询数值
     *
     */
    Integer selectForContest(ContestProblemSubmissionsCalculateExample contestProblemSubmissionsCalculateExample);

    @Select("select * from contest_problem where problem_num=#{problemNum} and contest_num=#{contestNum}")
    ContestProblemDO selectByProblemNum(@Param("problemNum") Integer problemNum,@Param("contestNum")Integer contestNum);

    @Select("select * from contest_problem where contest_num=#{contestNum} order by problem_letter_index asc")
    List<ContestProblemDO> selectByContestNum(Integer contestNum);

    void batchInsert(List<ContestProblemDO> contestProblemDOS);

    ContestProblemCalculation getBasicCacl(ContestProblemDTO request);
}
