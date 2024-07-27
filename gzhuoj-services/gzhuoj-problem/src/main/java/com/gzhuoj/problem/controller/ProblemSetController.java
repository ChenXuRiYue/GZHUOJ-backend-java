package com.gzhuoj.problem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.problem.dto.req.ProblemSetRequestDTO;
import com.gzhuoj.problem.dto.resp.ProblemSetResponseDTO;
import com.gzhuoj.problem.service.ProblemSetService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/problemset")
public class ProblemSetController {
    private final ProblemSetService problemSetService;
    @GetMapping("")
    public Result<IPage<ProblemSetResponseDTO>> all(ProblemSetRequestDTO requestParam){
        return Results.success(problemSetService.all(requestParam));
    }
}
