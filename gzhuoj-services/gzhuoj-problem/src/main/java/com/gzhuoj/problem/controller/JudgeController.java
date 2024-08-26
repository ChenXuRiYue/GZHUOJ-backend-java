package com.gzhuoj.problem.controller;

import com.gzhuoj.problem.constant.PathConstant;
import com.gzhuoj.problem.constant.PatternConstant;
import com.gzhuoj.problem.dto.req.judge.ListJudgeDataReqDTO;
import com.gzhuoj.problem.service.judge.JudgeService;
import com.gzhuoj.problem.service.common.UploadService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.ADMIN_UPLOAD_ILLEGAL_FILE_ERROR;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-problem/judge")
public class JudgeController {
    private final JudgeService judgeService;
    private final UploadService uploadService;
    /**
     * 题目测试数据管理/展示
     * @param requestParam 关键字
     */
    @GetMapping("/data_manager")
    public Result<Void> judgeDataManager(ListJudgeDataReqDTO requestParam) {
        judgeService.judgeDataManager(requestParam);
        return Results.success();
    }

    /**
     * testCase测试数据上传
     * @param problemNum 题目编号
     * @param testCase 测试数据
     */
    @PostMapping(value = "/upload")
    public Result<Void> upload(@RequestParam("problemNum") Integer problemNum, @RequestPart("testCase") List<MultipartFile> testCase){
        Boolean Fail = uploadService.upload(problemNum, testCase, PathConstant.PROBLEM_TESTCASE_PATH ,Pattern.compile(PatternConstant.TESTCASE_PATTERN));
        if(Fail){
            return Results.failure(ADMIN_UPLOAD_ILLEGAL_FILE_ERROR.code(), ADMIN_UPLOAD_ILLEGAL_FILE_ERROR.message());
        }
        return Results.success();
    }


}
