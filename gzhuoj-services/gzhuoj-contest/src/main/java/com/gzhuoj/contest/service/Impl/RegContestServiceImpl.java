package com.gzhuoj.contest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.contest.dto.req.RegContestGenTeamReqDTO;
import com.gzhuoj.contest.dto.req.RegContestLoginReqDTO;
import com.gzhuoj.contest.dto.req.RegContestLogoutReqDTO;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;
import com.gzhuoj.contest.mapper.TeamMapper;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.service.ContestService;
import com.gzhuoj.contest.service.RegContestService;
import common.convention.errorcode.BaseErrorCode;
import common.exception.ClientException;
import common.toolkit.GenerateRandStrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static common.convention.errorcode.BaseErrorCode.TEAM_LOGIN_ACCOUNT_ERROR;
import static common.convention.errorcode.BaseErrorCode.TEAM_LOGIN_PASSWORD_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegContestServiceImpl implements RegContestService {
    private final TeamMapper teamMapper;
    private final ContestService contestService;

    @Value("${RegContest.max-gen-team}")
    private Integer MAX_GEN_TEAM;

    @Override
//    @Transactional
    public List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam) {
        // 当reset为on时删除cid的所有team重新载入
        if(requestParam.getCid() == null){
            throw new ClientException("请传入比赛编号");
        }

        if(contestService.queryByNum(requestParam.getCid()) == null){
            throw new ClientException("比赛不存在");
        }
        if(Objects.equals(requestParam.getReset(), "on")){
            LambdaQueryWrapper<TeamDO> deleteWrapper = Wrappers.lambdaQuery(TeamDO.class)
                    .eq(TeamDO::getContestId, requestParam.getCid());
            teamMapper.delete(deleteWrapper);
        }

        String description = requestParam.getDescription();

        List<String> teamList = StrUtil.isEmpty(description) ? new ArrayList<>() : Arrays.asList(description.split("\n"));
        List<RegContestGenTeamRespDTO> teamRespList = new ArrayList<>();
        List<TeamDO> resultTeam = new ArrayList<>();
        String teamPrefix = "Team";
        if(teamList.size() == 1){ // 只有一行可能是根据数量生成空队伍
            String[] line = teamList.get(0).split("[#\\t]");
            if(line.length == 1) {
                Integer teamNum = null;
                if (StrUtil.isNumeric(line[0])) {
                    teamNum = Math.min(Integer.parseInt(line[0]), MAX_GEN_TEAM);
                }
                if(teamNum != null) {
                    for (int i = 0; i < teamNum; i++) {
                        TeamDO teamDO = new TeamDO();
                        teamDO.setContestId(requestParam.getCid());
                        teamDO.setTeamStatus(0);
                        teamDO.setTeamType(0);
                        teamDO.setTeamId(StrUtil.join("_", teamPrefix, leadZero(Integer.toString(i), 4)));
                        teamDO.setPassword(GenerateRandStrUtil.getRandStr(8));
                        resultTeam.add(teamDO);
                        teamRespList.add(BeanUtil.toBean(teamDO, RegContestGenTeamRespDTO.class));
                    }
                    resultTeam.forEach(teamMapper::insertOrUpdateTeam);
                    return teamRespList;
                }
            }
        }

        if(teamList.isEmpty()){
            throw new ClientException("请输入至少一个team表单或team数量");
        }
        if(teamList.size() > MAX_GEN_TEAM){
            throw new ClientException("队伍数量过多");
        }
        String[] fieldList = {"team_id", "team_name", "school", "team_member", "coach", "room", "team_type", "password", "contest_id"};
        int fieldNum = fieldList.length;
        boolean ok = true;
        for(String teamStr : teamList){
            String[] teamInfo = teamStr.split("[#\\t]");
            int infoLen = teamInfo.length;
            TeamDO teamDO = new TeamDO();
            teamDO.setTeamStatus(0);
            for(int i = 0; i < fieldNum; i++ ){
                //  输入的字符串格式不一定完全一样
                String field = i < infoLen ? teamInfo[i].trim() : "";
                int lastTeamNum = teamMapper.getLastTeamNum(requestParam.getCid());
                String curTeamNum = leadZero(Integer.toString(lastTeamNum), 4);
                switch (fieldList[i]){
                    case "team_id" -> {
                        if(!StrUtil.isEmpty(field)){
                            curTeamNum = field;
                        }
                        teamDO.setTeamId(teamPrefix + curTeamNum);
                    }
                    case "team_name" -> {
                        if(field.length() > 100){
                            log.error((teamPrefix + curTeamNum) + "的team_name长度太长，不能超过100个字符");
                            ok = false;
                        }
                        teamDO.setTeamName(field);
                    }
                    case "school" -> {
                        teamDO.setSchool(field);
                    }
                    case "team_member" -> {
                        teamDO.setTeamMember(field);
                    }
                    case "coach" -> {
                        teamDO.setCoach(field);
                    }
                    case "room" -> {
                        teamDO.setRoom(field);
                    }
                    case "team_type" -> {
                        int type = Integer.parseInt(field.isEmpty() ? "0" : field);
                        if (type > 2 || type < 0) {
                            type = 0;
                        }
                        teamDO.setTeamType(type);
                    }
                    case "password" -> {
                        if(StrUtil.isEmpty(field)){
                            field = GenerateRandStrUtil.getRandStr(8);
                        }
                        teamDO.setPassword(field);
                    }
                    case "contest_id" -> {
                        teamDO.setContestId(requestParam.getCid());
                    }
                    // 去除了权限的设置，改到由super_admin自己设置
                }
            }
            resultTeam.add(teamDO);
            teamRespList.add(BeanUtil.toBean(teamDO, RegContestGenTeamRespDTO.class));
        }
        if(!ok){
            throw new ClientException("部分team信息参数错误，整体回滚");
        }
        resultTeam.forEach(teamMapper::insertOrUpdateTeam);
        return teamRespList;
    }

    @Override
    public void login(RegContestLoginReqDTO requestParam) {
        // TODO 权限判断
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getTeamId, requestParam.getTeamId());
        TeamDO teamDO = teamMapper.selectOne(queryWrapper);
        if(teamDO == null){
            throw new ClientException(TEAM_LOGIN_ACCOUNT_ERROR);
        }
        if(!Objects.equals(teamDO.getPassword(), requestParam.getPassword())){
            throw new ClientException(TEAM_LOGIN_PASSWORD_ERROR);
        }
        // TODO
    }

    @Override
    public void logout(RegContestLogoutReqDTO requestParam) {
        // TODO
    }


    private String leadZero(String s, int len) {
        StringBuilder sBuilder = new StringBuilder(s);
        while(sBuilder.length() != len){
            sBuilder.insert(0, "0");
        }
        return sBuilder.toString();
    }
}
