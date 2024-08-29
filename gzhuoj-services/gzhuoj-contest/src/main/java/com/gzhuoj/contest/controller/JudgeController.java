package com.gzhuoj.contest.controller;

import com.gzhuoj.contest.dto.req.Judge.RegContestJudgeSubmitReqDTO;
import com.gzhuacm.sdk.contest.model.dto.SubmitRemoteDTO;
import com.gzhuoj.contest.service.judge.JudgeService;
import com.gzhuoj.contest.service.judge.SubmitService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_SUBMIT_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest")
public class JudgeController {
    private final JudgeService judgeService;
    private final SubmitService submitService;
    private final DiscoveryClient discoveryClient;
    /**
     * 提交代码 -- 链路起点
     */
    @PostMapping("/submit")
    public Result<Void> submit(@RequestBody RegContestJudgeSubmitReqDTO requestParam){
        judgeService.submit(requestParam);
        return Results.success();
    }

    /**
     * submitDO更新 用于远程调用
     */
    @PutMapping("/submit")
    public Result<Boolean> submitUpdate(@RequestBody SubmitRemoteDTO requestParam){
        return Results.success(submitService.updateSubmitDO(requestParam));
    }

    @GetMapping("/code")
    public Result<String> getCode(Integer submitId){
        return Results.success(submitService.getCode(submitId));
    }
}
