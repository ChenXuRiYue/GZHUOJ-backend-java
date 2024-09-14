package com.gzhuoj.contest.controller;

import com.gzhuacm.sdk.problem.model.dto.ProblemContentRespDTO;
import com.gzhuoj.contest.dto.req.contest.ContestUpdateInfoPushReqDTO;
import com.gzhuoj.contest.dto.resp.contestProblem.ContestResultRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest")
public class ContestProblemController {
    private final ContestProblemService contestProblemService;
    @GetMapping("/problem")
    public Result<List<ContestProblemDO>> getAllProblem(Integer contestNum){
        return Results.success(contestProblemService.getAllProblem(contestNum));
    }

    @GetMapping("/getresult")
    public Result<ContestResultRespDTO> getAllResult(Integer contestNum){
        return Results.success(contestProblemService.getResult(contestNum));
    }

    /**
     * 传入比赛Id , 以及题目在比赛中的相对序号，获取到题目的内容。
     *
     * @param contestNum        比赛序号
     * @param problemNumInContest 题目在比赛中的相对序号
     * @return 返回一个涵盖题目大多数内容的信息。
     */
    @GetMapping("/get/problem/content")
    public Result<ProblemContentRespDTO> getContestProblem(Integer contestNum, Integer problemNumInContest){
        return Results.success(contestProblemService.getContestProblem(contestNum, problemNumInContest));
    }

    /**
     * 将比赛题目的编排编号映射成全局中实际的题目编号
     * @return 题目编号
     */
    @GetMapping("/global")
    public Result<Integer> queryGlobalNumByLetter(Integer contestNum, Integer problemLetterIndex){
        return Results.success(contestProblemService.queryProNumByLetterId(contestNum, problemLetterIndex));
    }

}
