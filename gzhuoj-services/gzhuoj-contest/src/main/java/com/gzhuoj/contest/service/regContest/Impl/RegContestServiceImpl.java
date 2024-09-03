package com.gzhuoj.contest.service.regContest.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.contest.config.JwtProperties;
import com.gzhuoj.contest.dto.req.regContest.*;
import com.gzhuoj.contest.dto.resp.regContest.*;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.mapper.TeamMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.model.pojo.PersonSeat;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import com.gzhuoj.contest.service.contest.ContestService;
import com.gzhuoj.contest.service.regContest.RegContestService;
import com.gzhuoj.contest.util.JwtTool;
import common.enums.SubmissionLanguage;
import common.redis.RedisKeyUtil;
import common.redis.RedisUtil;
import common.biz.user.UserContext;
import common.enums.SubmissionStatus;
import common.exception.ClientException;
import common.exception.ServiceException;
import common.exception.UnauthorizedException;
import common.utils.GenerateRandStrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.gzhuoj.common.sdk.model.pojo.Option;
import org.gzhuoj.common.sdk.model.pojo.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


import static common.constant.RedisKey.*;
import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegContestServiceImpl implements RegContestService {
    private final TeamMapper teamMapper;
    private final ContestService contestService;
    private final SubmitMapper submitMapper;
    private final ContestMapper contestMapper;
    private final ProblemApi problemApi;
    private final ContestProblemService contestProblemService;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${RegContest.max-gen-team}")
    private Integer MAX_GEN_TEAM;

    @Override
//    @Transactional
    public List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam) {
        // 当reset为on时删除contestNum的所有team重新载入
        if(requestParam.getContestNum() == null){
            throw new ClientException("请传入比赛编号");
        }

        if(contestService.queryByNum(requestParam.getContestNum()) == null){
            throw new ClientException("比赛不存在");
        }
        if(Objects.equals(requestParam.getReset(), "on")){
            LambdaQueryWrapper<TeamDO> deleteWrapper = Wrappers.lambdaQuery(TeamDO.class)
                    .eq(TeamDO::getContestNum, requestParam.getContestNum());
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
                        teamDO.setContestNum(requestParam.getContestNum());
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
        String[] fieldList = {"team_id", "team_name", "school", "team_member", "coach", "room", "team_type", "password", "contest_num"};
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
                int lastTeamNum = teamMapper.getLastTeamNum(requestParam.getContestNum());
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
                    case "contest_num" -> {
                        teamDO.setContestNum(requestParam.getContestNum());
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
        ContestDO contestDO = contestService.queryByNum(teamDO.getContestNum());
        if(contestDO == null){
            throw new ClientException(CONTEST_NOT_FOUND_ERROR);
        }
        if(!Objects.equals(teamDO.getPassword(), requestParam.getPassword())){
            throw new ClientException(TEAM_LOGIN_PASSWORD_ERROR);
        }

        if( !Objects.equals(UserContext.getRole(), "3")){
            String token = jwtTool.createToken(requestParam.getTeamAccount(), 3, jwtProperties.getTokenTTL());
            response.addHeader("token", token);
        }
        return new RegContestLoginRespDTO(teamDO.getTeamAccount(), teamDO.getTeamName());
    }

    @Override
    public void logout(RegContestLogoutReqDTO requestParam) {

    }

    @Override
    public void deleteTeam(RegContestDelTeamReqDTO requestParam) {
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount())
                .eq(TeamDO::getContestNum, requestParam.getContestNum());
        teamMapper.delete(queryWrapper);
    }

    @Override
    public void updateTeam(RegContestUpdateTeamReqDTO requestParam) {
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getContestNum, requestParam.getContestNum())
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
                .eq(TeamDO::getContestNum, requestParam.getContestNum())
                .eq(TeamDO::getTeamAccount, requestParam.getTeamAccount());
        TeamDO hasTeamDO = teamMapper.selectOne(queryWrapper);
        if(hasTeamDO == null){
            throw new ClientException(TEAM_INFO_NOT_FOUND_ERROR);
        }
        RegContestTeamInfoRespDTO bean = BeanUtil.toBean(hasTeamDO, RegContestTeamInfoRespDTO.class);
        bean.setContestNum(requestParam.getContestNum());
        return bean;
    }

    @Override
    public IPage<RegContestStatusRespDTO> status(RegContestStatusReqDTO requestParam) {
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getContestNum, requestParam.getContestNum());
        if(requestParam.getProblemNum() != null){
            queryWrapper.eq(SubmitDO::getProblemNum, requestParam.getProblemNum());
        }
        if(requestParam.getSchool() != null){
            queryWrapper.eq(SubmitDO::getLanguage, requestParam.getSchool());
        }
        if(requestParam.getStatus() != null){
            queryWrapper.eq(SubmitDO::getStatus, requestParam.getStatus());
        }
        if(!StrUtil.isEmpty(requestParam.getTeamAccount())){
            queryWrapper.like(SubmitDO::getTeamAccount, requestParam.getTeamAccount());
        }
        ContestDO contestDO = contestService.queryByNum(requestParam.getContestNum());
        // team看不到封榜后的提交
        if(Objects.equals(UserContext.getRole(), "3")){
            Date endTime = contestDO.getEndTime();
            endTime = addTime(endTime, -(contestDO.getFrozenMinute()));
            queryWrapper.between(SubmitDO::getSubmitTime, contestDO.getStartTime(), endTime);
        }
        queryWrapper.orderBy(true, true, SubmitDO::getSubmitTime);
        IPage<SubmitDO> result = submitMapper.selectPage(requestParam, queryWrapper);

        return result.convert(each -> BeanUtil.toBean(each, RegContestStatusRespDTO.class));
    }

    // TODO 改造， 将两者分离。 并且优化缓存方案
    // 创建 contest 首页题目集里列表： 这意味着要将更多的信息展示，总体上为 problemList + 选手过题情况 + 个人过题情况
    @Override
    public List<RegContestProblemRespDTO> getContestProblemSetView(RegContestProSetReqDTO requestParam) {
        // 判断合法性
        ContestDO contestDO = contestService.queryByNum(requestParam.getContestNum());
        if (contestDO == null) {
            throw new ClientException(CONTEST_NOT_FOUND_ERROR);
        }
        // 查出contestProblemSet 注意是公共数据: 题目基本信息， 不涉及选手过题情况， 个人过题情况等。
        // 该函数已经处理了是否缓存的情况。
        ArrayList<RegContestProblemRespDTO> result = (ArrayList<RegContestProblemRespDTO>) getRegProblemSet(requestParam.getContestNum());

        // 查出选手过题情况预处理。特殊处理封榜情况，处理contestDO 的时间，
        initTimeInterval(contestDO);
        // 计算选手过题情况
        calculateProblemsSubmissionDetails(result, contestDO);

        return result;
    }

    @Override
    public List<RegContestProblemRespDTO> getRegProblemSet(Integer contestNum) {
        ArrayList<RegContestProblemRespDTO> result = getRegProblemSetByRedis(contestNum);
        if (ObjectUtils.isEmpty(result)) {
            result = getRegProblemSetBydatabase(contestNum);
            // 调整顺序且缓存
            result.sort(Comparator.comparingInt(RegContestProblemRespDTO::getProblemLetterIndex));
            cacheRegProblemSetByRedis(contestNum, result);
        }
        return result;
    }

    /**
     * @param contestNum
     * @return
     */
    public ArrayList<RegContestProblemRespDTO> getRegProblemSetBydatabase(Integer contestNum) {
        //

        // 获取题目列表和颜色
        // 题目在比赛中实际的位置 多表查询
        List<ContestProblemDO> allProblem = contestProblemService.getAllProblem(contestNum);
        ArrayList<RegContestProblemRespDTO> result = new ArrayList<>();
        for (ContestProblemDO cpDO : allProblem) {
            ProblemRespDTO problemRespDTO = problemApi.queryProByNum(cpDO.getProblemNum()).getData();
            if (problemRespDTO == null) {
                throw new ServiceException(SERVICE_PROBLEM_NOT_FOUND_ERROR);
            }
            RegContestProblemRespDTO regContestProblemRespDTO = RegContestProblemRespDTO.builder()
                    .problemNum(cpDO.getProblemNum())
                    .problemLetterIndex(cpDO.getProblemLetterIndex())
                    .memoryLimit(problemRespDTO.getMemoryLimit())
                    .timeLimit(problemRespDTO.getTimeLimit())
                    .problemName(problemRespDTO.getProblemName())
                    .build();
            result.add(regContestProblemRespDTO);
        }
        return result;
    }

    // TODO 写入缓存
    // TODO 总结一套完整的序列化配置方案，防止出现不同的配置情况。
    @Override
    public ArrayList<RegContestProblemRespDTO> getRegProblemSetByRedis(Integer contestNum) {
        String key = RedisKeyUtil.generateContestProblemSetKey(contestNum);
        RedisUtil redisUtil = new RedisUtil(stringRedisTemplate);
        Object jsonStr = stringRedisTemplate.opsForHash().get(key, REGULAR_CONTEST_PROBLEM_SET_HASH_KEY);
        ArrayList<RegContestProblemRespDTO> listFromHash = (ArrayList<RegContestProblemRespDTO>) redisUtil.getListFromHash(jsonStr, key, RegContestProblemRespDTO.class);
        if (CollUtil.isNotEmpty(listFromHash)) {
            return listFromHash;
        }
        return null;
    }


    // TODO 读入缓存
    public void cacheRegProblemSetByRedis(Integer contestNum, List<RegContestProblemRespDTO> target) {
        // 查询到结果后将结果缓存5s 缓解查询压力
        // TODO 减少重复创建导致的消耗。对客户端进行统一管理
        RedisUtil redisUtil = new RedisUtil(stringRedisTemplate);
        String key = RedisKeyUtil.generateContestProblemSetKey(contestNum);
        // 缓存10分钟
        redisUtil.saveListToHash(key, REGULAR_CONTEST_PROBLEM_SET_HASH_KEY, target, 600L, TimeUnit.SECONDS);
    }


    private void initTimeInterval(ContestDO contestDO) {
        // 设计submit状态对应表
        Date startTime = contestDO.getStartTime();
        Date endTime = contestDO.getEndTime();

        // TODO 测试加入用户上下文之后的结果
        if (Objects.equals(UserContext.getRole(), "3")) {
            // 管理员 -> 不封榜
            // sql -> 区间时间 -> groupBy teamName -> teamId -> contestNum ----> 个数
            // 非管理员 -> 封榜
            endTime = addTime(endTime, -(contestDO.getFrozenMinute()));
            // TODO 创建比赛时封榜开始的时间不能早于比赛开始时间
            if (endTime.before(startTime)) {
                endTime = startTime;
            }
        }
        // 将更新后的查询时间重用到contestDO 中
        contestDO.setStartTime(startTime);
        contestDO.setEndTime(endTime);
    }

    /**
     * 计算过题情况
     */
    public void calculateProblemsSubmissionDetails(List<RegContestProblemRespDTO> target, ContestDO contestDO) {
        for (RegContestProblemRespDTO respDTO : target) {
            calculatePersonalProblemPassDetails(respDTO, contestDO);
            calculateGlobalProblemPassDetails(respDTO, contestDO);
        }
    }

    /**
     * 这几个量实时性较强，并且不好拆分，因此暂时不做缓存方案
     *
     * @param respDTO
     * @param contestDO
     */
    public void calculatePersonalProblemPassDetails(RegContestProblemRespDTO respDTO, ContestDO contestDO) {
        // 该用户是否AC 无提交则无颜色变化 WA -> 红  AC -> 绿
        boolean AC = false;
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getContestNum, contestDO.getContestNum())
                .eq(SubmitDO::getProblemNum, respDTO.getProblemNum())
                .eq(SubmitDO::getTeamAccount, UserContext.getUserId())
                .eq(SubmitDO::getStatus, SubmissionStatus.ACCEPTED);
        SubmitDO submitDO = submitMapper.selectOne(queryWrapper);
        if (submitDO != null) {
            AC = true;
        }
        respDTO.setAC(AC);
    }

    public void calculateGlobalProblemPassDetails(RegContestProblemRespDTO respDTO, ContestDO contestDO) {
        LambdaQueryWrapper<SubmitDO> submitDOLambdaQueryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .select(SubmitDO::getTeamAccount)
                .eq(SubmitDO::getContestNum, contestDO.getContestNum())
                .eq(SubmitDO::getProblemNum, respDTO.getProblemNum())
                .between(SubmitDO::getSubmitTime, contestDO.getStartTime(), contestDO.getEndTime())
                .groupBy(SubmitDO::getTeamAccount);
        List<SubmitDO> submitDOS = submitMapper.selectList(submitDOLambdaQueryWrapper);
        respDTO.setAccepted(submitDOS.size());
    }


    /**
     * 给当前时间加若干分钟
     * @param current 基准时间
      * @param addMinute 增加的时间 -- 可以为负数
     * @return 最终时间
     */
    public Date addTime(Date current, Integer addMinute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.MINUTE, addMinute);
        return calendar.getTime();
    }

    @Override
    public ContestDO getContest(Integer contestNum) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestNum, contestNum)
                .eq(ContestDO::getDeleteFlag, 0);
        return contestMapper.selectOne(queryWrapper);
    }

    @Override
    public ContestSeatRespDTO contestSeat(Integer contestNum, ContestSeatReqDTO reqDTO) {
        ContestSeatRespDTO respDTO = new ContestSeatRespDTO();
        respDTO.personSeats=new ArrayList<>();
        List<TeamDO> teamDOS = contestMapper.teamSelectByContestNum(contestNum);
        Collections.shuffle(teamDOS);
        int examinationNum = reqDTO.examinationName.size();
        int eNowId=0,eNowSeat=0;//目前分配到的考场与座位号
        for (TeamDO teamDO : teamDOS) {
            String members=teamDO.getTeamMember();
            String[] member = members.split(",");
            int length = member.length;
            while (eNowId<examinationNum && eNowSeat+length>reqDTO.examination.get(eNowId)){
                eNowId++;eNowSeat=0;
            }
            if (eNowId>=examinationNum){
                throw new UnauthorizedException("考场座位不足！");
            }
            for (String s : member) {
                eNowSeat++;
                PersonSeat personSeat = new PersonSeat();
                personSeat.setName(s);
                personSeat.setSeat(eNowSeat);
                personSeat.setExamination(reqDTO.examinationName.get(eNowId));
                personSeat.setTeamName(teamDO.getTeamName());
                respDTO.personSeats.add(personSeat);
            }
        }
        return respDTO;
    }


    @Override
    public ContestWaitRespDTO waitTime(ContestWaitReqDTO requestParam) {
        //比赛等待时间查询
        Integer contestNum = requestParam.getContestNum();
        String teamAccount = requestParam.getTeamAccount();

        ContestDO contestDO = contestMapper.selectByContestNum(contestNum);
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
        result.teamTotal=teamMapper.teamTotalByContestNum(contestNum);
        return result;
    }

    private String leadZero(String s, int len) {
        StringBuilder sBuilder = new StringBuilder(s);
        while(sBuilder.length() != len){
            sBuilder.insert(0, "0");
        }
        return sBuilder.toString();
    }

    @Override
    public Options<String, Integer> getLanguageOptions(Integer contestNum) {
        long languageMask = getContestLanguageMask(contestNum);
        List<Option<String, Integer>> languageOptions = SubmissionLanguage.getLanguageOptionListByCode(languageMask);
        return new Options<>(languageOptions);
    }

    public long getContestLanguageMask(Integer contestNum) {
        LambdaQueryWrapper<ContestDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ContestDO::getContestNum, contestNum);
        return contestMapper.selectOne(lambdaQueryWrapper).getLanguageMask();
    }

    @Override
    public Options<String, Integer> getProblemOptions(Integer contestNum) {
        List<RegContestProblemRespDTO> regContestProblemRespDTOS = getRegProblemSet(contestNum);
        return contestProblemsToProblemOptions(regContestProblemRespDTOS);
    }

    public Options<String, Integer> contestProblemsToProblemOptions(List<RegContestProblemRespDTO> regContestProblemRespDTOS) {
        // ep: A. 题目名称 , ProblemLetterIndex; -> 实际编号不轻易暴露给用户；
        Options<String, Integer> res = new Options<>();
        for (RegContestProblemRespDTO regContestProblem : regContestProblemRespDTOS) {
            Option<String, Integer> option = new Option<>();
            option.setKey((char) (regContestProblem.getProblemLetterIndex() + 'A') + ":" + regContestProblem.getProblemName());
            option.setValue(regContestProblem.getProblemLetterIndex());
            res.add(option);
        }
        return res;
    }

}
