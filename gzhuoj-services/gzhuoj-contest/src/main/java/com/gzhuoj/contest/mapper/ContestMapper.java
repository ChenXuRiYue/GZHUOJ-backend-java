package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import org.apache.ibatis.annotations.Select;

public interface ContestMapper extends BaseMapper<ContestDO> {
    @Select("SELECT * from contest where contest_id=#{contestId}")
    ContestDO selectByContestId(Integer contestId);
}
