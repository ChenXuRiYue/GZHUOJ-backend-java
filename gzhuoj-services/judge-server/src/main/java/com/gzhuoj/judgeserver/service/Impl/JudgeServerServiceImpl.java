package com.gzhuoj.judgeserver.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.judgeserver.judge.JudgeContext;
import com.gzhuoj.judgeserver.mapper.JudgeServerMapper;
import com.gzhuoj.judgeserver.model.entity.JudgeServerDO;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.remote.DTO.ContestRemoteService;
import com.gzhuoj.judgeserver.remote.DTO.ProblemRemoteService;
import com.gzhuoj.judgeserver.remote.DTO.req.SubmitRemoteDTO;
import com.gzhuoj.judgeserver.remote.DTO.resp.ProblemRespDTO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import common.convention.result.Result;
import common.enums.SubmissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JudgeServerServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServerDO> implements JudgeServerService {
    private final ContestRemoteService contestRemoteService;
    private final ProblemRemoteService problemRemoteService;
    private final JudgeContext judgeContext;

    @Value("${gzhuoj-judge-server.name}")
    private String name;

    @Override
    public HashMap<String, Object> getJudgeServerInfo() {
        return null;
    }

    @Override
    public void judge(SubmitDO submitDO) {
        // 判断进入编译阶段
        SubmitRemoteDTO submitRemoteDTO = SubmitRemoteDTO.builder()
                .judger(name)
                .submitId(submitDO.getSubmitId())
                .status(SubmissionStatus.STATUS_COMPILING.getCode())
                .build();
        Result<Boolean> booleanResult = contestRemoteService.submitUpdate(submitRemoteDTO);
        if(!booleanResult.getData()){
            return;
        }
        Result<ProblemRespDTO> problemRespDTO = problemRemoteService.queryProByNum(submitDO.getProblemId());
        SubmitDO submitResult = judgeContext.judge(problemRespDTO.getData(), submitDO);
        contestRemoteService.submitUpdate(BeanUtil.toBean(submitResult, SubmitRemoteDTO.class));
    }
}
