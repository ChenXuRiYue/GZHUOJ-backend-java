package com.gzhuoj.problem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.problem.constant.PathConstant;
import com.gzhuoj.problem.constant.PatternConstant;
import com.gzhuoj.problem.dto.req.problem.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.ListProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.ProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.UpdateProblemReqDTO;
import com.gzhuoj.problem.dto.resp.problem.ListProblemRespDTO;
import com.gzhuoj.problem.dto.resp.problem.ProblemContentRespDTO;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.service.problem.ProblemService;
import com.gzhuoj.problem.service.common.UploadService;
import common.convention.result.Result;
import common.convention.result.Results;
import common.model.pojo.ProblemPrint;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

import static common.convention.errorcode.BaseErrorCode.ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR;

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
    public Result<ProblemDO> queryProByNum(Integer num){
        return Results.success(problemService.queryProByNum(num));
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
        Boolean Fail = uploadService.upload(problemNum, testCase, PathConstant.PROBLEM_UPLOAD_PATH, Pattern.compile(PatternConstant.PROBLEM_DESCRIPTION_PATTERN));
        if(Fail){
            return Results.failure(ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR.code(), ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR.message());
        }
        return Results.success();
    }

    @GetMapping("/selectProblemById")
    public ProblemPrint selectProblemById(Integer problemId){
        //System.out.printf("调试%d",problemId);
        ProblemPrint result=new ProblemPrint();
        result.setProblemDO(problemService.selectProblemById(problemId));
        result.setProblemDescrDO(problemService.selectProblemDescrById(problemId));
        result.setTestExampleDO(problemService.selectTestExampleById(problemId));
        return result;
    }
}
