package com.gzhuoj.contest.service.contestProblem.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.pojo.CPResult;
import com.gzhuoj.contest.model.pojo.SFC;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblemDO> implements ContestProblemService {
    @Autowired
    ContestProblemMapper contestProblemMapper;
    @Autowired
    ContestMapper contestMapper;
    @Override
    public List<ContestProblemDO> getAllProblem(Integer cid) {
        LambdaQueryWrapper<ContestProblemDO> queryWrapper = Wrappers.lambdaQuery(ContestProblemDO.class)
                .eq(ContestProblemDO::getContestId, cid);
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

        ContestDO CDO = contestMapper.selectByContestId(contestId);
        Date startTime = CDO.getStartTime();
        Date endTime = CDO.getEndTime();


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
        return contestResultRespDTO;
    }


}
