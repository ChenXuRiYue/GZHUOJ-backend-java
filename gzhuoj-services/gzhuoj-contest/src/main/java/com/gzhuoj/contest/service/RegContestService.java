package com.gzhuoj.contest.service;

import com.gzhuoj.contest.dto.req.RegContestGenTeamReqDTO;
import com.gzhuoj.contest.dto.req.RegContestLoginReqDTO;
import com.gzhuoj.contest.dto.req.RegContestLogoutReqDTO;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;

import java.util.List;

public interface RegContestService {
    List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam);

    void login(RegContestLoginReqDTO requestParam);

    void logout(RegContestLogoutReqDTO requestParam);
}
