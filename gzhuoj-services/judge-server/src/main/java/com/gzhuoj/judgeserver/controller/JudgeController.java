package com.gzhuoj.judgeserver.controller;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import com.gzhuoj.judgeserver.dto.req.ToJudgeReqDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_PARAM_NOT_FOUND_ERROR;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-judge-server")
public class JudgeController {
    private final JudgeServerService judgeServerService;

    @PostMapping("/judge")
    public Result<Void> judge(@RequestBody ToJudgeReqDTO requestParam){
        if(requestParam.getSubmitDTO() == null || requestParam.getJudgeServerIp() == null || requestParam.getJudgeServerPort() == null){
            log.error("Judge Param error, ToJudgeReqDTO = {}", requestParam);
            throw new ServiceException(JUDGE_PARAM_NOT_FOUND_ERROR);
        }
        SubmitDO submitDO = BeanUtil.toBean(requestParam.getSubmitDTO(), SubmitDO.class);
        judgeServerService.judge(submitDO);
        return Results.success().setMessage("成功发送！");
    }
}
