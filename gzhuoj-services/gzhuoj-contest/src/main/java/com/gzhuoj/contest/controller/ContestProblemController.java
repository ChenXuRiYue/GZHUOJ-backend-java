package com.gzhuoj.contest.controller;

import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.remote.Resp.ProblemContentRespDTO;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import common.convention.result.Result;
import common.convention.result.Results;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest")
public class ContestProblemController {
    private final ContestProblemService contestProblemService;
    @GetMapping("/problem")
    public Result<List<ContestProblemDO>> getAllProblem(Integer cid){
        return Results.success(contestProblemService.getAllProblem(cid));
    }

    @GetMapping("/getresult")
    public Result<ContestResultRespDTO> getAllResult(Integer cid){
        return Results.success(contestProblemService.getResult(cid));
    }

    /**
     * 传入比赛Id , 以及题目在比赛中的相对序号，获取到题目的内容。
     * @param contestId  比赛序号
     * @param contestProblemId 题目在比赛中的相对序号
     * @return 返回一个涵盖题目大多数内容的信息。
     */
    @PostMapping("/get/problem/contests")
    public Result<ProblemContentRespDTO> getContestProblem(@PathParam("contestId") Integer contestId, @PathParam("contestProblemId") Integer contestProblemId){
        return Results.success(contestProblemService.getContestProblem(contestId, contestProblemId));
    }
}
