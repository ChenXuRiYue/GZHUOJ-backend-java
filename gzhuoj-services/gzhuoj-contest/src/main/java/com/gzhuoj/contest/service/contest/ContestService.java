package com.gzhuoj.contest.service.contest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.dto.req.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.ContestStatusReqDTO;
import com.gzhuoj.contest.dto.req.ContestUpdateReqDTO;
import com.gzhuoj.contest.dto.resp.ContestAllRespDTO;
import com.gzhuoj.contest.model.entity.ContestDO;

public interface ContestService extends IService<ContestDO> {
    void update(ContestUpdateReqDTO requestParam);

    void create(ContestCreateReqDTO requestParam);

    ContestDO queryByNum(Integer num);

    IPage<ContestAllRespDTO> all(ContestAllReqDTO requestParam);

    void changeStatus(ContestStatusReqDTO requestParam);
}
