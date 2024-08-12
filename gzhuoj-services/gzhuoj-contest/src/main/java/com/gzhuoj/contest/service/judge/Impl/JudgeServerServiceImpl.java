package com.gzhuoj.contest.service.judge.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.mapper.JudgeServerMapper;
import com.gzhuoj.contest.model.entity.JudgeServerDO;
import com.gzhuoj.contest.service.judge.JudgeServerService;
import org.springframework.stereotype.Service;

@Service
public class JudgeServerServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServerDO> implements JudgeServerService {
}
