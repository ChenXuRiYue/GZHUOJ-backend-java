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
import lombok.extern.slf4j.Slf4j;
import org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.*;

/**
 * 操作前置校验
 */
@Slf4j
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
    @Transactional
    public void contestPreCheckAndSave(RegContestJudgeSubmitReqDTO requestParam, SubmitDO submitDO) {
        ContestDO contest = Optional.ofNullable(regContestService.getContest(requestParam.getContestNum()))
                .orElseThrow(() -> new ClientException(CONTEST_NOT_FOUND_ERROR));

        checkContestStartTime(contest);
        validateTeamAndProblem(requestParam, contest, submitDO);

        // 插入提交和源代码
        submitMapper.insert(submitDO);
        submitCodeMapper.insert(new SubmitCodeDO(submitDO.getSubmitId(), requestParam.getCode()));
    }

    /**
     * 校验比赛是否开始
     */
    private void checkContestStartTime(ContestDO contest) {
        Date curTime = new Date();
        if (Objects.equals(UserContext.getRole(), "3") && contest.getStartTime().after(curTime)) {
            throw new ClientException(CONTEST_NOT_START);
        }
    }

    /**
     * 校验队伍是否有效以及问题是否存在
     */
    private void validateTeamAndProblem(RegContestJudgeSubmitReqDTO requestParam, ContestDO contest, SubmitDO submitDO) {
        if (!isAdminRole() && !validTeam(requestParam.getContestNum(), requestParam.getTeamAccount())) {
            throw new ClientException(CONTEST_TEAM_NOT_FOUND);
        }

        Optional.ofNullable(requestParam.getProblemNum())
                .orElseThrow(() -> new ClientException(CONTEST_PROBLEM_MAP_IS_NULL_ERROR));

        submitDO.setProblemNum(contestProblemService.queryGobleNumByLetter(requestParam.getContestNum(), requestParam.getProblemNum()));

        if (problemApi.queryProByNum(submitDO.getProblemNum()) == null) {
            throw new ClientException(PROBLEM_NOT_FOUND);
        }
    }

    /**
     * 判断是否为管理员
     */
    private boolean isAdminRole() {
        return !Objects.equals(UserContext.getRole(), "3");
    }

    /**
     * 验证队伍是否存在
     */
    private boolean validTeam(Integer contestNum, String teamAccount) {
        LambdaQueryWrapper<TeamDO> queryWrapper = Wrappers.lambdaQuery(TeamDO.class)
                .eq(TeamDO::getContestNum, contestNum)
                .eq(TeamDO::getTeamAccount, teamAccount);
        return teamMapper.selectOne(queryWrapper) != null;
    }
}
