package com.gzhuoj.contest.service.judge;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.contest.model.entity.SubmitDO;

public interface SubmitService extends IService<SubmitDO> {
    /**
     * 根据submitId 查询提交对象实体
     */
    SubmitDO getSubmitDO(Integer submitId);
}
