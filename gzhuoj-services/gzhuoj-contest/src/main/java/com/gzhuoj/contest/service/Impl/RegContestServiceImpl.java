package com.gzhuoj.contest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.contest.config.JwtProperties;
import com.gzhuoj.contest.constant.SubmissionStatus;
import com.gzhuoj.contest.dto.req.*;
import com.gzhuoj.contest.dto.resp.ContestWaitRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestProSetRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestStatusRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestTeamInfoRespDTO;
import com.gzhuoj.contest.dto.resp.*;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.mapper.TeamMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.remote.ProblemRemoteService;
import com.gzhuoj.contest.remote.Resp.ProblemRespDTO;
import com.gzhuoj.contest.service.ContestProblemService;
import com.gzhuoj.contest.service.ContestService;
import com.gzhuoj.contest.service.RegContestService;
import com.gzhuoj.contest.utils.JwtTool;
import com.gzhuoj.contest.utils.RedisUtil;
import common.biz.user.UserContext;
import common.exception.ClientException;
import common.exception.ServiceException;
import common.toolkit.GenerateRandStrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


import static com.gzhuoj.contest.constant.RedisKey.REGULAR_CONTEST_PROBLEM_SET;
import static common.convention.errorcode.BaseErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegContestServiceImpl implements RegContestService {
    private final TeamMapper teamMapper;
    private final ContestService contestService;
    private final SubmitMapper submitMapper;
    private final ContestMapper contestMapper;
    private final ProblemRemoteService problemRemoteService;
    private final ContestProblemService contestProblemService;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisUtil redisUtil;
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
        String teamPrefix = "team";
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
                        teamDO.setTeamAccount(teamPrefix + leadZero(Integer.toString(i), 4));
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
                        teamDO.setTeamAccount(teamPrefix + curTeamNum);
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
    public RegContestLoginRespDTO login(RegContestLoginReqDTO requestParam, HttpServletResponse response) {
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount());
        TeamDO teamDO = teamMapper.selectOne(queryWrapper);
        if(teamDO == null){
            throw new ClientException(TEAM_LOGIN_ACCOUNT_ERROR);
        }
        if(!Objects.equals(teamDO.getPassword(), requestParam.getPassword())){
            throw new ClientException(TEAM_LOGIN_PASSWORD_ERROR);
        }

        if(!Objects.equals(UserContext.getRole(), "admin")){
            String token = jwtTool.createToken(requestParam.getTeamAccount(), 3, jwtProperties.getTokenTTL());
            response.addHeader("token", token);
        }
        return new RegContestLoginRespDTO(teamDO.getTeamAccount(), teamDO.getTeamName());
    }

    @Override
    public void logout(RegContestLogoutReqDTO requestParam) {
        // TODO
    }

    @Override
    public void deleteTeam(RegContestDelTeamReqDTO requestParam) {
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount())
                .eq(TeamDO::getContestId, requestParam.getCid());
        teamMapper.delete(queryWrapper);
    }

    @Override
    public void updateTeam(RegContestUpdateTeamReqDTO requestParam) {
        // TODO 抽取查询队伍是否存在
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getContestId, requestParam.getCid())
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount());
        TeamDO hasTeamDO = teamMapper.selectOne(queryWrapper);
        if(hasTeamDO == null){
            throw new ClientException(TEAM_UPDATE_NOT_FOUND_ERROR);
        }
        String newPassword = requestParam.getNewPassword();
        if(!StrUtil.isEmpty(newPassword)){
            if(newPassword.length() < 6){
                throw new ClientException(TEAM_UPDATE_LOW_PASSWORD_ERROR);
            }
        }
        Integer teamType = requestParam.getTeamType();
        if(teamType != null){
            if(teamType < 0 || teamType > 2){
                teamType = 0;
            }
        }
        // TODO privilege判断
        TeamDO teamDO = TeamDO.builder()
                .teamName(requestParam.getTeamName())
                .teamMember(requestParam.getTeamMember())
                .coach(requestParam.getCoach())
                .school(requestParam.getSchool())
                .room(requestParam.getRoom())
                .teamType(teamType)
                .teamPrivilege(requestParam.getTeamPrivilege())
                .password(newPassword)
                .build();
        LambdaUpdateWrapper<TeamDO> updateWrapper = Wrappers.lambdaUpdate(TeamDO.class)
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount());
        teamMapper.update(teamDO, updateWrapper);
    }

    @Override
    public RegContestTeamInfoRespDTO teamInfo(RegContestTeamInfoReqDTO requestParam) {
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getContestId, requestParam.getCid())
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount());
        TeamDO hasTeamDO = teamMapper.selectOne(queryWrapper);
        if(hasTeamDO == null){
            throw new ClientException(TEAM_INFO_NOT_FOUND_ERROR);
        }
        RegContestTeamInfoRespDTO bean = BeanUtil.toBean(hasTeamDO, RegContestTeamInfoRespDTO.class);
        bean.setCid(requestParam.getCid());
        return bean;
    }

    @Override
    public IPage<RegContestStatusRespDTO> status(RegContestStatusReqDTO requestParam) {
        // TODO 要判断当前team的比赛编号是否和传入的比赛编号相同
        // TODO 要判断当前线程team的编号是否和传入的team编号相同
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getContestId, requestParam.getContestId());
        if(requestParam.getProblemId() != null){
            queryWrapper.eq(SubmitDO::getProblemId, requestParam.getProblemId());
        }
        if(requestParam.getLanguage() != null){
            queryWrapper.eq(SubmitDO::getLanguage, requestParam.getLanguage());
        }
        if(requestParam.getStatus() != null){
            queryWrapper.eq(SubmitDO::getStatus, requestParam.getStatus());
        }
        if(!StrUtil.isEmpty(requestParam.getTeamAccount())){
            queryWrapper.like(SubmitDO::getTeamAccount, requestParam.getTeamAccount());
        }
        queryWrapper.orderBy(true, true, SubmitDO::getSubmitTime);
        IPage<SubmitDO> result = submitMapper.selectPage(requestParam, queryWrapper);
        return result.convert(each -> BeanUtil.toBean(each, RegContestStatusRespDTO.class));
    }

    @Override
    public List<RegContestProSetRespDTO> problemSet(RegContestProSetReqDTO requestParam) {
        RedisUtil redisUtil = new RedisUtil(stringRedisTemplate);
        String key = REGULAR_CONTEST_PROBLEM_SET + UserContext.getUserId();
        Object jsonStr = stringRedisTemplate.opsForHash().get(key, requestParam.getCid().toString());
        List<RegContestProSetRespDTO> listFromHash = redisUtil.getListFromHash(jsonStr, key, RegContestProSetRespDTO.class);
        if(CollUtil.isNotEmpty(listFromHash)){
            return listFromHash;
        }
        ContestDO contestDO = contestService.queryByNum(requestParam.getCid());
        if(contestDO == null){
            throw new ClientException(CONTEST_NOT_FOUND_ERROR);
        }

        // 获取题目列表和颜色
        // 题目在比赛中实际的位置 多表查询
        List<ContestProblemDO> allProblem = contestProblemService.getAllProblem(requestParam.getCid());
        ArrayList<RegContestProSetRespDTO> result = new ArrayList<>();
        for(ContestProblemDO cpDO : allProblem){
            ProblemRespDTO problemRespDTO = problemRemoteService.queryProByNum(cpDO.getProblemId());
            if(problemRespDTO == null){
                throw new ServiceException(SERVICE_PROBLEM_NOT_FOUND_ERROR);
            }
            RegContestProSetRespDTO regContestProSetRespDTO = RegContestProSetRespDTO.builder()
                    .color(cpDO.getProblemColor())
                    .problemNum(cpDO.getProblemId())
                    .actualNum(cpDO.getActualNum())
                    .memoryLimit(problemRespDTO.getMemoryLimit())
                    .timeLimit(problemRespDTO.getTimeLimit())
                    .problemName(problemRespDTO.getProblemName())
                    .build();
            result.add(regContestProSetRespDTO);
        }
        // 设计submit状态对应表

        Date startTime = contestDO.getStartTime();
        Date endTime = contestDO.getEndTime();
        // TODO 测试加入用户上下文之后的结果
        if(!Objects.equals(UserContext.getRole(), "3")){
            // 管理员 -> 不封榜
            // sql -> 区间时间 -> groupBy teamName -> teamId -> contestId ----> 个数
            // 非管理员 -> 封榜
            endTime = addTime(endTime, -(contestDO.getFrozenMinute()));
            // TODO 创建比赛时封榜开始的时间不能早于比赛开始时间
            if(endTime.before(startTime)){
                endTime = startTime;
            }
        }

        for(RegContestProSetRespDTO respDTO : result){
            // 该用户是否AC 无提交则无颜色变化 WA -> 红  AC -> 绿
            boolean AC = false;
            LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                    .eq(SubmitDO::getContestId, requestParam.getCid())
                    .eq(SubmitDO::getProblemId, respDTO.getProblemNum())
                    .eq(SubmitDO::getTeamAccount, UserContext.getUserId())
                    .eq(SubmitDO::getStatus, SubmissionStatus.ACCEPTED);
            SubmitDO submitDO = submitMapper.selectOne(queryWrapper);
            if(submitDO != null){
                AC = true;
            }
            LambdaQueryWrapper<SubmitDO> submitDOLambdaQueryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                    .select(SubmitDO::getTeamAccount)
                    .eq(SubmitDO::getContestId, requestParam.getCid())
                    .eq(SubmitDO::getProblemId, respDTO.getProblemNum())
                    .between(SubmitDO::getSubmitTime, startTime, endTime)
                    .groupBy(SubmitDO::getTeamAccount);
            List<SubmitDO> submitDOS = submitMapper.selectList(submitDOLambdaQueryWrapper);
            respDTO.setAccepted(submitDOS.size());
            respDTO.setAC(AC);
        }
        result.sort(Comparator.comparingInt(RegContestProSetRespDTO::getActualNum));
        // 查询到结果后将结果缓存10s 缓解查询压力
        redisUtil.saveListToHash(key, requestParam.getCid().toString(), result, 10L, TimeUnit.SECONDS);
        return result;

    }

    public Date addTime(Date current, Integer addMinute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.MINUTE, addMinute);
        return calendar.getTime();
    }
    @Override
    public Boolean exist(Integer cid) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestId, cid)
                .eq(ContestDO::getDeleteFlag, 0);
        ContestDO contestDO = contestMapper.selectOne(queryWrapper);
        return contestDO != null;
    }


    @Override
    public ContestWaitRespDTO waitTime(ContestWaitReqDTO requestParam) {
        //比赛等待时间查询
        Integer contestId = requestParam.getContestId();
        String teamAccount = requestParam.getTeamAccount();

        ContestDO contestDO = contestMapper.selectByContestId(contestId);
        TeamDO teamDO = teamMapper.selectByTeamAccount(teamAccount);

        Date startTime = contestDO.getStartTime();
        Date nowTime = new Date();
        if (startTime.getTime() < nowTime.getTime()) {
            throw new ClientException(CONTEST_HAVE_BEGIN);
        }
        long restTime = startTime.getTime() - nowTime.getTime();

        ContestWaitRespDTO result = new ContestWaitRespDTO();
        result.days=restTime/(1000*60*60*24); restTime%=(1000*60*60*24);
        result.hours=restTime/(1000*60*60); restTime%=(1000*60*60);
        result.minutes=restTime/(1000*60); restTime%=(1000*60);
        result.seconds=restTime/1000; restTime%=1000;

        result.contestName=contestDO.getTitle();
        result.teamName= teamDO.getTeamName();
        result.teamTotal=teamMapper.teamTotalByContestId(contestId);
        return result;
    }


    private String leadZero(String s, int len) {
        StringBuilder sBuilder = new StringBuilder(s);
        while(sBuilder.length() != len){
            sBuilder.insert(0, "0");
        }
        return sBuilder.toString();
    }
}
