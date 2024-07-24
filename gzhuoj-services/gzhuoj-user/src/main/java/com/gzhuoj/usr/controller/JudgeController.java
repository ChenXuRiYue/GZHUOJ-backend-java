package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.dto.req.JudgeUploadCaseReqDTO;
import com.gzhuoj.usr.service.JudgeService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测评控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj")
public class JudgeController {
    private final JudgeService judgeService;

    @PostMapping("/admin/judge/upload")
    public Result<List<String>> uploadJudgeCase(JudgeUploadCaseReqDTO requestParam){
        return Results.success(judgeService.uploadJudgeCase(requestParam));
    }
}
