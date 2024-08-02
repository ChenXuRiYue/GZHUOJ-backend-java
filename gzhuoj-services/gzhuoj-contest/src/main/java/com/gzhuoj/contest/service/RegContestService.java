package com.gzhuoj.contest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.dto.req.*;
import com.gzhuoj.contest.dto.resp.ContestWaitRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestProSetRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestStatusRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestTeamInfoRespDTO;

import java.util.List;

public interface RegContestService {
    List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam);

    void login(RegContestLoginReqDTO requestParam);

    void logout(RegContestLogoutReqDTO requestParam);

    void deleteTeam(RegContestDelTeamReqDTO requestParam);

    void updateTeam(RegContestUpdateTeamReqDTO requestParam);

    RegContestTeamInfoRespDTO teamInfo(RegContestTeamInfoReqDTO requestParam);

    IPage<RegContestStatusRespDTO> status(RegContestStatusReqDTO requestParam);


    ContestWaitRespDTO waitTime(ContestWaitReqDTO requestParam);

    List<RegContestProSetRespDTO> problemSet(RegContestProSetReqDTO requestParam);

    Boolean exist(Integer cid);

}
