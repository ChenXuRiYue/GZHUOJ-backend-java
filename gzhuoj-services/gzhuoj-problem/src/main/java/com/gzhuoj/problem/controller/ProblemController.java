package com.gzhuoj.problem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuacm.sdk.problem.model.dto.ProblemContentRespDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemPrintDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.problem.constant.PathConstant;
import org.gzhuoj.common.sdk.constant.PatternConstant;
import com.gzhuoj.problem.dto.req.problem.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.ListProblemReqDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.UpdateProblemReqDTO;
import com.gzhuoj.problem.dto.resp.problem.ListProblemRespDTO;
import com.gzhuoj.problem.service.problem.ProblemService;
import com.gzhuoj.problem.service.common.UploadService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-problem/problem")
public class ProblemController {
    private final ProblemService problemService;
    private final UploadService uploadService;
    /**
     * 创建题目、题目单独更新，减小操作颗粒度
     *
     * @param requestParam
     * @return
     */
    @PostMapping("/create")
    public Result<Void> createProblem(@RequestBody CreateProblemReqDTO requestParam) {
        problemService.createProblem(requestParam);
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

    /**
     * 根据题目编号查询题目信息
     * @param num 题目编号
     * @return 题目信息实体
     */
    @GetMapping("/query")
    public Result<ProblemRespDTO> queryProByNum(Integer problemNum){
        return Results.success(problemService.queryProByNum(problemNum));
    }

    /**
     * 查出题目的所有内容： 面向题面展示的一次性所有信息查询
     * @return
     */

    // TODO 远程调用命名可能不一致。注意排查
    @PostMapping("/get/contents")
    public Result<ProblemContentRespDTO> getProblemContent(@RequestBody ProblemReqDTO request){
        return Results.success(problemService.getProblemContent(request.getProblemNum()));
    }

    /**
     * 根据题目编号上传题目附件
     * @param problemNum 题目编号
     * @param testCase
     * @return
     */
    @PostMapping("/uploadDescr")
    public Result<Void> uploadDescr(@RequestParam("problemNum") Integer problemNum, @RequestPart("testCase") List<MultipartFile> testCase){
        uploadService.upload(problemNum, testCase, PathConstant.PROBLEM_UPLOAD_PATH, Pattern.compile(PatternConstant.PROBLEM_DESCRIPTION_PATTERN));
        return Results.success();
    }



    @GetMapping("/selectProblemById")
    public ProblemPrintDTO selectProblemById(Integer problemNum){
        //System.out.printf("调试%d",problemNum);
        ProblemPrintDTO result=new ProblemPrintDTO();
        result.setProblemDO(problemService.selectProblemById(problemNum));
        result.setProblemDescrDO(problemService.selectProblemDescrById(problemNum));
        result.setTestExampleDO(problemService.selectTestExampleById(problemNum));
        return result;
    }
}
