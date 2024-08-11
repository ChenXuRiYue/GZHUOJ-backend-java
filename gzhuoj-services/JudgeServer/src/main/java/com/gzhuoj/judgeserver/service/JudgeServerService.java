package com.gzhuoj.judgeserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.judgeserver.model.entity.JudgeServerDO;

import java.util.HashMap;

public interface JudgeServerService extends IService<JudgeServerDO> {
    HashMap<String, Object> getJudgeServerInfo();
}
