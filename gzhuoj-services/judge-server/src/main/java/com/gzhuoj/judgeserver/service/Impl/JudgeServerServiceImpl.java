package com.gzhuoj.judgeserver.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuacm.sdk.judgeserver.api.ContestRemoteService;
import com.gzhuacm.sdk.judgeserver.model.dto.req.SubmitRemoteDTO;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.judgeserver.judge.JudgeContext;
import com.gzhuoj.judgeserver.mapper.JudgeServerMapper;
import com.gzhuoj.judgeserver.model.entity.JudgeServerDO;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import common.Handler.WebSocketMessageHandler;
import common.biz.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.gzhuoj.common.sdk.convention.result.Result;
import common.enums.SubmissionStatus;
import lombok.RequiredArgsConstructor;
import org.gzhuoj.common.sdk.convention.result.Results;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgeServerServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServerDO> implements JudgeServerService {
    private final ContestRemoteService contestRemoteService;
    private final ProblemApi problemRemoteService;
    private final JudgeContext judgeContext;
    private final WebSocketMessageHandler webSocketMessageHandler;

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
            log.error("Failed to update submit status for submitId = {}", submitDO.getSubmitId());
            return;
        }
        Result<ProblemRespDTO> problemRespResult = problemRemoteService.queryProByNum(submitDO.getProblemNum());
        if (!problemRespResult.isSuccess()) {
            log.error("Failed to query problem for problemNum = {}", submitDO.getProblemNum());
            return;
        }
        SubmitDO submitResult = judgeContext.judge(problemRespResult.getData(), submitDO);
        Result<Boolean> updateStatus = contestRemoteService.submitUpdate(BeanUtil.toBean(submitResult, SubmitRemoteDTO.class));
        // 如果是非服务端错误的状态则向前端发送实时信息
        if (updateStatus.getData()){
            String statusStr = SubmissionStatus.getStatusStr(submitResult.getStatus());
            if(statusStr != null){
                webSocketMessageHandler.sendMessageToOne(UserContext.getUserId(), submitDO.getContestNum(), statusStr);
            }
        }
    }
}
