package com.gzhuoj.contest.service.regContest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.dto.req.regContest.*;
import com.gzhuoj.contest.dto.resp.regContest.*;
import com.gzhuoj.contest.model.entity.ContestDO;
import jakarta.servlet.http.HttpServletResponse;
import org.gzhuoj.common.sdk.model.pojo.Options;

import java.util.ArrayList;
import java.util.List;

public interface RegContestService {
    List<RegContestGenTeamRespDTO> genTeam(RegContestGenTeamReqDTO requestParam);

    RegContestLoginRespDTO login(RegContestLoginReqDTO requestParam, HttpServletResponse response);

    void logout(RegContestLogoutReqDTO requestParam);

    void deleteTeam(RegContestDelTeamReqDTO requestParam);

    void updateTeam(RegContestUpdateTeamReqDTO requestParam);

    RegContestTeamInfoRespDTO teamInfo(RegContestTeamInfoReqDTO requestParam);

    IPage<RegContestStatusRespDTO> status(RegContestStatusReqDTO requestParam);

    ContestWaitRespDTO waitTime(ContestWaitReqDTO requestParam);

    List<RegContestProblemRespDTO> getContestProblemSetView(RegContestProSetReqDTO requestParam);


    List<RegContestProblemRespDTO> getRegProblemSet(Integer contestNum);

    // TODO 写入缓存
    // TODO 总结一套完整的序列化配置方案，防止出现不同的配置情况。
    ArrayList<RegContestProblemRespDTO> getRegProblemSetByRedis(Integer contestNum);

    ContestDO getContest(Integer contestNum);

    ContestSeatRespDTO contestSeat(Integer contestNum, ContestSeatReqDTO reqDTO);

    Options<String, Integer> getLanguageOptions(Integer contestNum);

    Options<String, Integer> getProblemOptions(Integer contestNum);
}
