package com.gzhuoj.contest.service.contestProblem.Impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemContentRespDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemReqDTO;
import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.pojo.CPResult;
import com.gzhuoj.contest.model.pojo.SFC;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import common.biz.user.UserContext;
import common.exception.ServiceException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.PROBLEM_MESSAGE_LOST;

@Service
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblemDO> implements ContestProblemService {
    @Resource
    ContestProblemMapper contestProblemMapper;
    @Resource
    ContestMapper contestMapper;
    @Resource
    StringRedisTemplate redis;
    @Resource
    ProblemApi problemApi;

    @Override
    public List<ContestProblemDO> getAllProblem(Integer contestId) {
        LambdaQueryWrapper<ContestProblemDO> queryWrapper = Wrappers.lambdaQuery(ContestProblemDO.class)
                .eq(ContestProblemDO::getContestId, contestId);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询某一题的信息
     * @param contestId
     * @param problemId
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public CPResult getProblemResult(Integer contestId, Integer problemId, Date beginTime, Date endTime) {
        SFC sfc = new SFC();
        sfc.beginTime=beginTime;
        sfc.endTime=endTime;
        sfc.contestId=contestId;
        sfc.problemId=problemId;

        CPResult cpResult = new CPResult();
        ContestProblemDO contestProblemDO = contestProblemMapper.selectByProblemId(problemId,contestId);
        cpResult.name = String.valueOf((char)('A'+contestProblemDO.getActualNum()));
        sfc.status=0;cpResult.ac=contestProblemMapper.selectForContest(sfc);
        sfc.status=1;cpResult.pe=contestProblemMapper.selectForContest(sfc);
        sfc.status=2;cpResult.wa=contestProblemMapper.selectForContest(sfc);
        sfc.status=3;cpResult.tle=contestProblemMapper.selectForContest(sfc);
        sfc.status=4;cpResult.mle=contestProblemMapper.selectForContest(sfc);
        sfc.status=5;cpResult.ole=contestProblemMapper.selectForContest(sfc);
        sfc.status=6;cpResult.re=contestProblemMapper.selectForContest(sfc);
        sfc.status=7;cpResult.ce=contestProblemMapper.selectForContest(sfc);
        sfc.status=null;cpResult.total=contestProblemMapper.selectForContest(sfc);

        sfc.language=0;cpResult.c=contestProblemMapper.selectForContest(sfc);
        sfc.language=1;cpResult.cPlus=contestProblemMapper.selectForContest(sfc);
        sfc.language=2;cpResult.java=contestProblemMapper.selectForContest(sfc);
        sfc.language=3;cpResult.python=contestProblemMapper.selectForContest(sfc);
        sfc.language=4;cpResult.go=contestProblemMapper.selectForContest(sfc);


        return cpResult;
    }

    @Override
    public ContestResultRespDTO getResult(Integer contestId) {
        //String role = UserContext.getRole();

        String role;
        role= UserContext.getRole();
        //role="3";
        if (role.equals("3"))role="user";
        else                 role="admin";
        System.out.println("当前用户为："+role);
        if (Boolean.TRUE.equals(redis.hasKey(contestId + '_' + role))){
            String s = redis.opsForValue().get(contestId + '_' + role);
            ContestResultRespDTO resultRespDTO = JSONObject.parseObject(s, ContestResultRespDTO.class);
            System.out.println("成功命中缓存");
            return resultRespDTO;
        }
        ContestDO CDO = contestMapper.selectByContestId(contestId);
        Date startTime = CDO.getStartTime();
        Date endTime = CDO.getEndTime();

        if(role.equals("user")){
            long time = endTime.getTime();
            time-=3600;
            endTime.setTime(time);
        }

        ContestResultRespDTO contestResultRespDTO = new ContestResultRespDTO();
        contestResultRespDTO.problem=new ArrayList<>();

        List<ContestProblemDO> contestProblemDOS = contestProblemMapper.selectByContestId(contestId);
        CPResult total=new CPResult();
        total.name="total";
        for (ContestProblemDO CPD : contestProblemDOS) {
            Integer problemId = CPD.getProblemId();
            CPResult problemResult = getProblemResult(contestId, problemId, startTime, endTime);
            contestResultRespDTO.problem.add(problemResult);
            total.ac+=problemResult.ac;
            total.pe+=problemResult.pe;
            total.wa+=problemResult.wa;
            total.tle+=problemResult.tle;
            total.mle+=problemResult.mle;
            total.ole+=problemResult.ole;
            total.re+=problemResult.re;
            total.ce+=problemResult.ce;
            total.total+=problemResult.total;
            total.c+=problemResult.c;
            total.cPlus+=problemResult.cPlus;
            total.java+=problemResult.java;
            total.python+=problemResult.python;
            total.go+=problemResult.go;
        }
        contestResultRespDTO.problem.add(total);

        redis.opsForValue().set(contestId + '_' + role, JSONObject.toJSONString(contestResultRespDTO), Duration.ofSeconds(50));

        return contestResultRespDTO;
    }

    /**
     * 加一道关卡有利于题目校验
     *
     * @param
     * @return
     */
    @Override
    public ProblemContentRespDTO getContestProblem(Integer contestId, Integer problemIdInContest) {
        QueryWrapper<ContestProblemDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(Map.of("contest_id", contestId, "actual_num" , problemIdInContest));
        ContestProblemDO contestProblemDO = contestProblemMapper.selectOne(queryWrapper);
        if(ObjectUtils.isEmpty(contestProblemDO)){
            throw new  ServiceException(PROBLEM_MESSAGE_LOST);
        }
        ProblemReqDTO problemReqDTO = new ProblemReqDTO();
        problemReqDTO.setProblemNum(contestProblemDO.getProblemId());
        return problemApi.getProblemContent(problemReqDTO).getData();
    }
}
