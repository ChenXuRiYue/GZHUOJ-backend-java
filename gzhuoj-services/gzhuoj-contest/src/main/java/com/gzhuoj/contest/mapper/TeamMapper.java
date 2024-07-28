package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.contest.model.entity.TeamDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeamMapper extends BaseMapper<TeamDO> {
    Integer getLastTeamNum(Integer cid);

    void insertOrUpdateTeam(@Param("team") TeamDO team);
}
