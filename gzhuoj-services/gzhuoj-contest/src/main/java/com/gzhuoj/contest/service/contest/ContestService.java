package com.gzhuoj.contest.service.contest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.dto.req.contest.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestStatusReqDTO;
import com.gzhuoj.contest.dto.resp.contest.ContestAllRespDTO;
import com.gzhuoj.contest.dto.resp.contest.PrintProblemRespDTO;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;

import java.util.List;

public interface ContestService extends IService<ContestDO> {
    void update(ContestReqDTO requestParam);

    void createContest(ContestReqDTO requestParam);

    ContestDO queryByNum(Integer num);

    void changeStatus(ContestStatusReqDTO requestParam);

    List<SubmitDO> sumbitData(Integer contestNum);

    List<TeamDO> teamData(Integer contestNum);

    PrintProblemRespDTO printProblem(Integer contestNum);


    IPage<ContestAllRespDTO> contestsView(ContestAllReqDTO requestParam);
}
