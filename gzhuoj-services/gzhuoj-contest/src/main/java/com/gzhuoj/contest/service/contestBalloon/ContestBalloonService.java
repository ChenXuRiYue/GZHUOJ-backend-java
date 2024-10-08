package com.gzhuoj.contest.service.contestBalloon;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.dto.req.contestBalloon.ContestBalloonChangeStReqDTO;
import com.gzhuoj.contest.dto.req.contestBalloon.ContestBalloonQueueReqDTO;
import com.gzhuoj.contest.dto.resp.contestBalloon.ContestBalloonQueueRespDTO;
import com.gzhuoj.contest.model.entity.ContestBalloonDO;

import java.util.List;

public interface ContestBalloonService extends IService<ContestBalloonDO> {
    List<ContestBalloonQueueRespDTO> queue(ContestBalloonQueueReqDTO requestParam);

    void status(ContestBalloonChangeStReqDTO requestParam);
}
