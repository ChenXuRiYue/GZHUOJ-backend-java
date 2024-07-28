package com.gzhuoj.contest.service;

import com.gzhuoj.contest.dto.req.*;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;

import java.util.List;

public interface RegContestService {
    List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam);

    void login(RegContestLoginReqDTO requestParam);

    void logout(RegContestLogoutReqDTO requestParam);

    void deleteTeam(RegContestDelTeamReqDTO requestParam);

    void updateTeam(RegContestUpdateTeamReqDTO requestParam);
}
