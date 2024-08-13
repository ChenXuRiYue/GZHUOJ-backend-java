package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContestMapper extends BaseMapper<ContestDO> {
    @Select("SELECT * from contest where contest_id=#{contestId}")
    ContestDO selectByContestId(Integer contestId);

    @Select("SELECT * from submit")
    List<SubmitDO> sumbitSelectByContestId(Integer contestId);

    @Select("SELECT * from team")
    List<TeamDO> teamSelectByContestId(Integer contestId);
}
