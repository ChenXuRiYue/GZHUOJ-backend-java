package com.gzhuoj.contest.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gzhuoj.contest.dto.req.Judge.RegContestJudgeSubmitReqDTO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.remote.Req.SubmitRemoteDTO;
import com.gzhuoj.contest.service.judge.JudgeService;
import com.gzhuoj.contest.service.judge.SubmitService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import static common.convention.errorcode.BaseErrorCode.JUDGE_SUBMIT_ERROR;

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
        boolean submitRes = judgeService.submit(requestParam);
        if(!submitRes) {
            return Results.failure(JUDGE_SUBMIT_ERROR.code(), JUDGE_SUBMIT_ERROR.message());
        }
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
