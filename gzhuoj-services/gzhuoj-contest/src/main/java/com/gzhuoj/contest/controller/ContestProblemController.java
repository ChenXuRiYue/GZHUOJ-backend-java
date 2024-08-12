package com.gzhuoj.contest.controller;

import com.gzhuoj.contest.dto.resp.ContestResultRespDTO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
