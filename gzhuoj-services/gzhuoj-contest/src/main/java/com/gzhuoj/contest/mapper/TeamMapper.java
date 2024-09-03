package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzhuoj.contest.model.entity.TeamDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeamMapper extends BaseMapper<TeamDO> {
    Integer getLastTeamNum(Integer contestNum);

    void insertOrUpdateTeam(@Param("team") TeamDO team);

    @Select("SELECT * from team where team_account=#{teamAccount}")
    TeamDO selectByTeamAccount(String teamAccount);

    @Select("select count(*) from team where contest_num=#{contestNum}")
    Integer teamTotalByContestNum(Integer contestNum);

}
