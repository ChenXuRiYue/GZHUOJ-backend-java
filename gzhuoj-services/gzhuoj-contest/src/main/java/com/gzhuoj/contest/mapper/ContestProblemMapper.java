package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.contest.dto.resp.RegContestProSetRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContestProblemMapper extends BaseMapper<ContestProblemDO> {
    List<RegContestProSetRespDTO> getProblemSet(@Param("cid") Integer cid);
}
