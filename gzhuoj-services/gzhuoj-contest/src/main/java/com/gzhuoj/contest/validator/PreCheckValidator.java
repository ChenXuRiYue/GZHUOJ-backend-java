package com.gzhuoj.contest.validator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuoj.contest.dto.req.Judge.RegContestJudgeSubmitReqDTO;
import com.gzhuoj.contest.mapper.SubmitCodeMapper;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.mapper.TeamMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.SubmitCodeDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import com.gzhuoj.contest.service.regContest.RegContestService;
import common.biz.user.UserContext;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.*;

/**
 * 操作前置校验
 */
@Component
@RequiredArgsConstructor
public class PreCheckValidator {
    private final RegContestService regContestService;
    private final ProblemApi problemApi;
    private final TeamMapper teamMapper;
    private final SubmitMapper submitMapper;
    private final SubmitCodeMapper submitCodeMapper;
    private final ContestProblemService contestProblemService;

    /**
     * 比赛信息前置校验并且将提交数据放入数据库
     */
    public void contestPreCheckAndSave(RegContestJudgeSubmitReqDTO requestParam, SubmitDO submitDO){
        ContestDO contest = regContestService.getContest(requestParam.getContestNum());
        if(contest == null){
            // 比赛是否存在
            throw new ClientException(CONTEST_NOT_FOUND_ERROR);
        }

        // 非管理员
        Date curTime = new Date();
        if(Objects.equals(UserContext.getRole(), "3")){
            // 比赛是否开始
            if(contest.getStartTime().after(curTime)){
                throw new ClientException(CONTEST_NOT_START);
            }
            // team是否存在
            if(!validTeam(requestParam.getContestNum(), requestParam.getTeamAccount())){
                throw new ClientException(CONTEST_TEAM_NOT_FOUND);
            }
        }
        if(requestParam.getProblemNum() == null){
            throw new ClientException(CONTEST_PROBLEM_MAP_IS_NULL_ERROR);
        }
        submitDO.setProblemNum(contestProblemService.queryGobleNumByLetter(requestParam.getContestNum(), requestParam.getProblemNum()));
        if(problemApi.queryProByNum(submitDO.getProblemNum()) == null){
            // 题目是否存在
            throw new ClientException(PROBLEM_NOT_FOUND);
        }

        // 插入提交
        submitMapper.insert(submitDO);
        // 插入源代码
        submitCodeMapper.insert(new SubmitCodeDO(submitDO.getSubmitId(), requestParam.getCode()));
    }
    private boolean validTeam(Integer contestNum, String teamAccount){
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getContestNum, contestNum)
                .eq(TeamDO::getTeamAccount, teamAccount);
        return teamMapper.selectOne(queryWrapper) != null;
    }
}
