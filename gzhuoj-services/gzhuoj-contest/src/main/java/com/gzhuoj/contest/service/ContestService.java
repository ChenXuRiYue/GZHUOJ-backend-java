package com.gzhuoj.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.dto.req.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.ContestUpdateReqDTO;
import com.gzhuoj.contest.model.entity.ContestDO;

public interface ContestService extends IService<ContestDO> {
    void update(ContestUpdateReqDTO requestParam);

    void create(ContestCreateReqDTO requestParam);
}
