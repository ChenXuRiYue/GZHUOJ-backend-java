package com.gzhuoj.problem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.problem.dto.req.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.ListProblemReqDTO;
import com.gzhuoj.problem.dto.req.UpdateProblemReqDTO;
import com.gzhuoj.problem.dto.resp.ListProblemRespDTO;
import com.gzhuoj.problem.service.ProblemService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/problem")
public class ProblemController {
    private final ProblemService problemService;

    /**
     * 创建题目、题目单独更新，减小操作颗粒度
     *
     * @param createProblemReqDTO
     * @return
     */
    @PostMapping("/create")
    public Result<Void> createProblem(@RequestBody CreateProblemReqDTO createProblemReqDTO) {
        problemService.createProblem(createProblemReqDTO);
        return Results.success();
    }

    /**
     * 题目列表展示
     * @param requestParam search模糊匹配, 根据题目编号升降序
     * @return 题目集合
     */
    @GetMapping("/list")
    public Result<IPage<ListProblemRespDTO>> listProblem(ListProblemReqDTO requestParam) {
        return Results.success(problemService.listProblem(requestParam));
    }

    /**
     * 修改题目
     * @param requestParam 修改题目字段参数
     */
    @PostMapping("/update")
    public Result<Void> updateProblem(@RequestBody UpdateProblemReqDTO requestParam){
        problemService.updateProblem(requestParam);
        return Results.success();
    }


}
