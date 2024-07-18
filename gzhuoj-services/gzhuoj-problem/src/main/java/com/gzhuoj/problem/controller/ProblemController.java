package com.gzhuoj.problem.controller;

import com.gzhuoj.problem.dto.req.CreateProblemReqDTO;
import com.gzhuoj.problem.service.ProblemService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemController {
    private final ProblemService problemService;

    /**
     * 创建题目、题目单独更新，减小操作颗粒度
     * @param createProblemReqDTO
     * @return
     */
    @PostMapping("/create")
    public Result<Void> createProblem(@RequestBody CreateProblemReqDTO createProblemReqDTO){
        problemService.createProblem(createProblemReqDTO);
        return Results.success();
    }
}
