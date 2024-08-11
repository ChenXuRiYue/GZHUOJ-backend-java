package com.gzhuoj.judgeserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.judgeserver.mapper.JudgeServerMapper;
import com.gzhuoj.judgeserver.model.entity.JudgeServerDO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class JudgeServerServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServerDO> implements JudgeServerService {

    @Override
    public HashMap<String, Object> getJudgeServerInfo() {
        return null;
    }
}
