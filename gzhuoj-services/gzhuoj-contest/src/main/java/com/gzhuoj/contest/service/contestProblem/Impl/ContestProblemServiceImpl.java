package com.gzhuoj.contest.service.contestProblem.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemContentRespDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemReqDTO;
import com.gzhuoj.contest.dto.req.contestProblem.ContestResultReqDTO;
import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.pojo.ContestProblemCalculation;
import com.gzhuoj.contest.model.example.ContestProblemSubmissionsCalculateExample;
import com.gzhuoj.contest.service.contest.ContestService;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import common.biz.contant.RoleConstant;
import common.biz.user.UserContext;
import common.enums.Language;
import common.enums.SubmissionStatus;
import common.exception.ClientException;
import common.exception.ServiceException;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.*;

@Service
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblemDO> implements ContestProblemService {
    @Resource
    ContestService contestService;
    @Resource
    ContestProblemMapper contestProblemMapper;
    @Resource
    ContestMapper contestMapper;
    @Resource
    StringRedisTemplate redis;
    @Resource
    ProblemApi problemApi;

    @Override
    public List<ContestProblemDO> getAllProblem(Integer contestNum) {
        LambdaQueryWrapper<ContestProblemDO> queryWrapper = Wrappers.lambdaQuery(ContestProblemDO.class)
                .eq(ContestProblemDO::getContestNum, contestNum);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 题目列表
     * 给定题目列表查出一道题目的信息
     */
    @Override
    public List<ContestProblemCalculation> getContestProblemsResult(ContestResultReqDTO request) {

        List<ContestProblemCalculation> result = new ArrayList<>();
        for(int problemLetterIndex: request.getProblemLetterIndexes()) {
            ContestResultReqDTO constructRequest = BeanUtil.toBean(request, ContestResultReqDTO.class);
            constructRequest.setProblemLetterIndexes(List.of(problemLetterIndex));
            result.add(getContestProblemResult(constructRequest));
        }
        return result;
    }

    /**
     * 单道题目
     */
    public ContestProblemCalculation getContestProblemResult(ContestResultReqDTO request){
        ContestProblemCalculation x =  getContestProblemResultFocusLanguages(request);
        ContestProblemCalculation y = getContestProblemResultFocusStatus(request);
        return mergerContestProblemResult(x , y);
    }
    public ContestProblemCalculation mergerContestProblemResult(ContestProblemCalculation x, ContestProblemCalculation y){
        assert ObjectUtils.isNotEmpty(x);
        assert ObjectUtils.isNotEmpty(y);
        ContestProblemCalculation result = new ContestProblemCalculation();
        // 防止重要成员空指针
        x.preventNullPointer();
        y.preventNullPointer();
        // 合并languages
        Set<String> languagesGroup = x.getLanguageCalculation().keySet();
        languagesGroup.addAll(y.getLanguageCalculation().keySet());
        for(String language: languagesGroup){
            Integer totalX = x.getLanguageCalculation().getOrDefault(language, 0);
            Integer totalY = y.getLanguageCalculation().getOrDefault(language, 0);
            result.getLanguageCalculation().put(language, totalX + totalY);
        }
        //合并 status
        Set<String> statusGroup = x.getStatusCalculation().keySet();
        languagesGroup.addAll(y.getStatusCalculation().keySet());
        for(String status: statusGroup){
            Integer totalX = x.getStatusCalculation().getOrDefault(status, 0);
            Integer totalY = y.getStatusCalculation().getOrDefault(status, 0);
            result.getStatusCalculation().put(status, totalX + totalY);
            if(status.equals(SubmissionStatus.ACCEPTED.getStatus())){
                result.setAcTotal(totalX + totalY);
            }
            result.setTotal(totalX + totalY);
        }
        return result;
    }

    /**
     * init Contest Problem Submission Submissions calculate example basic Info
     * @param request
     * @return
     */
    public ContestProblemSubmissionsCalculateExample initContestProSubmissionsCaclExampleBasicInfo(ContestResultReqDTO request){
        ContestProblemSubmissionsCalculateExample example = new ContestProblemSubmissionsCalculateExample();
        example.setStartTime(DateUtil.date(request.getStartTimes()));
        example.setEndTime(DateUtil.date(request.getEndTimes()));
        example.setContestNum(request.getContestNum());
        assert(CollectionUtils.isNotEmpty(request.getProblemLetterIndexes()));
        example.setProblemNum(request.getProblemLetterIndexes().get(0));
        ContestProblemDO contestProblemDO = contestProblemMapper.selectByProblemNum(example.getProblemNum(), example.getContestNum());

        ContestProblemCalculation contestProblemCalculation = new ContestProblemCalculation();
        contestProblemCalculation.setProblemLetter(String.valueOf((char) ('A' + contestProblemDO.getProblemLetterIndex())));
        return example;
    }

    public ContestProblemCalculation getContestProblemResultFocusLanguages(ContestResultReqDTO request) {
        ContestProblemCalculation result = new ContestProblemCalculation();
        result.preventNullPointer();

        ContestProblemSubmissionsCalculateExample example = initContestProSubmissionsCaclExampleBasicInfo(request);
        // 不更新acTotal
        for(Language language: Language.values()){
            example.setLanguage(language.getCode());
            Integer targetSubmissionsAmount = contestProblemMapper.selectForContest(example);
            result.getLanguageCalculation().put(language.getLang(), targetSubmissionsAmount);
            result.setTotal(result.getTotal() + targetSubmissionsAmount);
        }
        return result;
    }

    public ContestProblemCalculation getContestProblemResultFocusStatus(ContestResultReqDTO request) {
        ContestProblemCalculation result = new ContestProblemCalculation();
        result.preventNullPointer();

        ContestProblemSubmissionsCalculateExample example = initContestProSubmissionsCaclExampleBasicInfo(request);
        // 不更新acTotal
        for(SubmissionStatus status: SubmissionStatus.values()){
            example.setStatus(status.getCode());
            Integer targetSubmissionsAmount = contestProblemMapper.selectForContest(example);
            result.getLanguageCalculation().put(status.getStatus(), targetSubmissionsAmount);
            result.setTotal(result.getTotal() + targetSubmissionsAmount);
            if(status.equals(SubmissionStatus.ACCEPTED)){
                result.setAcTotal(targetSubmissionsAmount);
            }
        }
        return result;
    }

    // TODO
    @Override
    public ContestResultRespDTO getResult(Integer contestNum) {
        //String role = UserContext.getRole();

//        String role;
//        role= UserContext.getRole();
//        //role="3";
//        if (role.equals("3"))role="user";
//        else                 role="admin";
//        System.out.println("当前用户为："+role);
//        if (Boolean.TRUE.equals(redis.hasKey(contestNum + '_' + role))){
//            String s = redis.opsForValue().get(contestNum + '_' + role);
//            ContestResultRespDTO resultRespDTO = JSONObject.parseObject(s, ContestResultRespDTO.class);
//            System.out.println("成功命中缓存");
//            return resultRespDTO;
//        }
//        ContestDO CDO = contestMapper.selectByContestNum(contestNum);
//        Date startTime = CDO.getStartTime();
//        Date endTime = CDO.getEndTime();
//
//        if(role.equals("user")){
//            long time = endTime.getTime();
//            time-=3600;
//            endTime.setTime(time);
//        }
//
//        ContestResultRespDTO contestResultRespDTO = new ContestResultRespDTO();
//        contestResultRespDTO.problem=new ArrayList<>();
//
//        List<ContestProblemDO> contestProblemDOS = contestProblemMapper.selectByContestNum(contestNum);
//        ContestProblemCalculation total=new ContestProblemCalculation();
//        total.name="total";
//        for (ContestProblemDO CPD : contestProblemDOS) {
//            Integer problemNum = CPD.getProblemNum();
//            ContestProblemCalculation problemResult = getContestProblemsResult(contestNum);
//            contestResultRespDTO.problem.add(problemResult);
//            total.ac+=problemResult.ac;
//            total.pe+=problemResult.pe;
//            total.wa+=problemResult.wa;
//            total.tle+=problemResult.tle;
//            total.mle+=problemResult.mle;
//            total.ole+=problemResult.ole;
//            total.re+=problemResult.re;
//            total.ce+=problemResult.ce;
//            total.total+=problemResult.total;
//            total.c+=problemResult.c;
//            total.cPlus+=problemResult.cPlus;
//            total.java+=problemResult.java;
//            total.python+=problemResult.python;
//            total.go+=problemResult.go;
//        }
//        contestResultRespDTO.problem.add(total);
//
//        redis.opsForValue().set(contestNum + '_' + role, JSONObject.toJSONString(contestResultRespDTO), Duration.ofSeconds(50));

//        return contestResultRespDTO;
        return null;
    }

    /**
     * 加一道关卡有利于题目校验
     *
     */
    @Override
    public ProblemContentRespDTO getContestProblem(Integer contestNum, Integer problemNumInContest) {
        QueryWrapper<ContestProblemDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(Map.of("contest_num", contestNum, "problem_letter_index" , problemNumInContest));
        ContestProblemDO contestProblemDO = contestProblemMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(contestProblemDO)){
            throw new  ServiceException(PROBLEM_MESSAGE_LOST);
        }
        ProblemReqDTO problemReqDTO = new ProblemReqDTO();
        problemReqDTO.setProblemNum(contestProblemDO.getProblemNum());
        return problemApi.getProblemContent(problemReqDTO).getData();
    }

    @Override
    public Integer queryProNumByLetterId(Integer contestNum, Integer problemLetterIndex) {
        if(contestNum == null || problemLetterIndex == null){
            throw new ClientException(CONTEST_PROBLEM_MAP_FAILURE_ERROR);
        }
        LambdaQueryWrapper<ContestProblemDO> queryWrapper = Wrappers.lambdaQuery(ContestProblemDO.class)
                .eq(ContestProblemDO::getProblemLetterIndex, problemLetterIndex)
                .eq(ContestProblemDO::getContestNum, contestNum);
        ContestProblemDO contestProblemDO = baseMapper.selectOne(queryWrapper);
        if (contestProblemDO.getProblemNum() == null){
            throw new ClientException(CONTEST_PROBLEM_MAP_NOT_EXISTED_ERROR);
        }
        return contestProblemDO.getProblemNum();
    }

    /**
     * 获取竞赛中题目的结果。方便缓存
     * 同时注意封榜的影响了。
     */
    @Override
    public List<ContestProblemCalculation> getContestProblemCalculation(ContestResultReqDTO contestResultReqDTO) {
        initContestResultRequestTimes(contestResultReqDTO);
        initContestResultRequestProblemLetterIndexes(contestResultReqDTO);
        initContestResultRequestLanguageCodeMask(contestResultReqDTO);
        return getContestProblemsResult(contestResultReqDTO);
    }

    /**
     * 初始化 ContestResultReqDTO 的时间： 情况用于封榜，权限等请求。
     * TODO 复用
     */
    public void initContestResultRequestTimes(ContestResultReqDTO contestResultReqDTO){
        // TODO initTime with Frozen Board Limit
        ContestDO contestDO = contestService.getContestDO(contestResultReqDTO.getContestNum());
        String role = UserContext.getRole();
        // 简单初始化
        contestResultReqDTO.setStartTimes(contestDO.getStartTime().getTime());
        contestResultReqDTO.setEndTimes(contestDO.getEndTime().getTime());
        // 封榜时间逻辑 TODO 区分普通队伍， TODO 普通选手获取榜单的一些细节要全局扫一遍
        // 覆盖修改
        if(role.equals(String.valueOf(RoleConstant.COMMON_USER))){
            // 更正封榜时间
            contestResultReqDTO.setEndTimes(contestResultReqDTO.getEndTimes() - 1000L * 60 * contestDO.getFrozenMinute());
        }
    }

    /**
     * 特判当index 为空时，将当前内容更新
     */
    public void initContestResultRequestProblemLetterIndexes(ContestResultReqDTO contestResultReqDTO){
        if(CollectionUtils.isEmpty(contestResultReqDTO.getProblemLetterIndexes())){
            contestResultReqDTO.setProblemLetterIndexes(new ArrayList<>());
            int contestProblemAmount = contestProblemMapper.selectByContestNum(contestResultReqDTO.getContestNum()).size();
            for(int i = 0; i < contestProblemAmount; i++){
                contestResultReqDTO.getProblemLetterIndexes().add(i);
            }
        }
    }

    public void initContestResultRequestLanguageCodeMask(ContestResultReqDTO contestResultReqDTO){
        // TODO initTime with Frozen Board Limit
        ContestDO contestDO = contestService.getContestDO(contestResultReqDTO.getContestNum());
        contestResultReqDTO.setLanguageMask(contestDO.getLanguageMask());
    }

}
