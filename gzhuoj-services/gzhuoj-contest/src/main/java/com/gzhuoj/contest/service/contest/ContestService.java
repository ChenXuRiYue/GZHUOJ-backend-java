package com.gzhuoj.contest.service.contest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.dto.req.contest.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestStatusReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestUpdateReqDTO;
import com.gzhuoj.contest.dto.resp.contest.ContestAllRespDTO;
import com.gzhuoj.contest.dto.resp.contest.PrintProblemRespDTO;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;

import java.util.List;

public interface ContestService extends IService<ContestDO> {
    void update(ContestUpdateReqDTO requestParam);

    void create(ContestCreateReqDTO requestParam);

    ContestDO queryByNum(Integer num);

    IPage<ContestAllRespDTO> all(ContestAllReqDTO requestParam);

    void changeStatus(ContestStatusReqDTO requestParam);

    List<SubmitDO> sumbitData(Integer contestId);

    List<TeamDO> teamData(Integer contestId);

    PrintProblemRespDTO printProblem(Integer contestId);
}
