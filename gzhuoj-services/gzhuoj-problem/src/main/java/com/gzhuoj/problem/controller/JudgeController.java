package com.gzhuoj.problem.controller;

import com.gzhuoj.problem.dto.req.ListJudgeDataReqDTO;
import com.gzhuoj.problem.service.JudgeService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static common.convention.errorcode.BaseErrorCode.USER_UPLOAD_ILLEGAL_FILE_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/judge")
public class JudgeController {
    private final JudgeService judgeService;

    /**
     * 题目测试数据管理/展示
     *
     * @param requestParam 关键字
     * @return 所有
     */
    @GetMapping("/data_manager")
    public Result<Void> judgeDataManager(ListJudgeDataReqDTO requestParam) {
        judgeService.judgeDataManager(requestParam);
        return Results.success();
    }


    @PostMapping(value = "/upload")
    public Result<Void> upload(Integer problem, List<MultipartFile> testCase){
        Boolean Fail = judgeService.upload(problem, testCase);
        if(Fail){
            return Results.failure(USER_UPLOAD_ILLEGAL_FILE_ERROR.code(), USER_UPLOAD_ILLEGAL_FILE_ERROR.message());
        }
        return Results.success();
    }


}
