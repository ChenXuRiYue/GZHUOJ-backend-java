package com.gzhuoj.contest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.dto.req.*;
import com.gzhuoj.contest.dto.resp.*;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.List;

public interface RegContestService {
    List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam);

    RegContestLoginRespDTO login(RegContestLoginReqDTO requestParam, HttpServletResponse response);

    void logout(RegContestLogoutReqDTO requestParam);

    void deleteTeam(RegContestDelTeamReqDTO requestParam);

    void updateTeam(RegContestUpdateTeamReqDTO requestParam);

    RegContestTeamInfoRespDTO teamInfo(RegContestTeamInfoReqDTO requestParam);

    IPage<RegContestStatusRespDTO> status(RegContestStatusReqDTO requestParam);

    List<RegContestProSetRespDTO> problemSet(RegContestProSetReqDTO requestParam);

    Boolean exist(Integer cid);

}
