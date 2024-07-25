package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.dto.req.JudgeUploadCaseReqDTO;
import com.gzhuoj.usr.remote.AdminRemoteService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 测评控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj")
public class JudgeController {
    private final AdminRemoteService adminRemoteService;

    @PostMapping(value = "/admin/judge/upload")
    public Result<Void> upload(@RequestParam("problemNum") Integer problemNum, @RequestPart("testCase") List<MultipartFile> testCase){
        return adminRemoteService.upload(problemNum, testCase);
    }
}
