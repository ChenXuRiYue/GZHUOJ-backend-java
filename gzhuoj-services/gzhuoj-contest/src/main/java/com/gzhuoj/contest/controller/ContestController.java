package com.gzhuoj.contest.controller;

import cn.hutool.core.bean.BeanUtil;
import com.gzhuoj.contest.dto.req.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.ContestUpdateReqDTO;
import com.gzhuoj.contest.service.ContestService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/contest")
public class ContestController {
    private final ContestService contestService;
    @PostMapping("/create")
    public Result<Void> create(@RequestBody ContestCreateReqDTO requestParam){
        contestService.create(requestParam);
        return Results.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody ContestUpdateReqDTO requestParam){
        contestService.update(requestParam);
        return Results.success();
    }
}
