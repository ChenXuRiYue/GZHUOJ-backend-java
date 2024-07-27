package com.gzhuoj.contest.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.constant.PathConstant;
import com.gzhuoj.contest.constant.PatternConstant;
import com.gzhuoj.contest.dto.req.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.ContestStatusReqDTO;
import com.gzhuoj.contest.dto.req.ContestUpdateReqDTO;
import com.gzhuoj.contest.dto.resp.ContestAllRespDTO;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.service.ContestService;
import com.gzhuoj.contest.service.UploadService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

import static common.convention.errorcode.BaseErrorCode.ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/contest")
public class ContestController {
    private final ContestService contestService;
    private final UploadService uploadService;
    /**
     * 创建比赛
     * @param requestParam 创建比赛参数
     */
    @PostMapping("/create")
    public Result<Void> create(@RequestBody ContestCreateReqDTO requestParam){
        contestService.create(requestParam);
        return Results.success();
    }

    /**
     * 更新比赛
     * @param requestParam 更新比赛参数
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody ContestUpdateReqDTO requestParam){
        contestService.update(requestParam);
        return Results.success();
    }

    /**
     * 根据编号查询比赛信息
     * @param num 比赛编号
     * @return 比赛信息实体
     */
    @GetMapping("/query")
    public Result<ContestDO> queryByNum(Integer num){
        return Results.success(contestService.queryByNum(num));
    }

    /**
     * 比赛附件上传
     * @param contestNum 比赛编号
     * @param description 比赛附件
     */
    @PostMapping("/uploadDescr")
    public Result<Void> uploadDescr(@RequestParam("contestNum") Integer contestNum, @RequestPart("description") List<MultipartFile> description){
        Boolean Fail = uploadService.upload(contestNum, description, PathConstant.CONTEST_UPLOAD_PATH, Pattern.compile(PatternConstant.CONTEST_DESCRIPTION_PATTERN));
        if(Fail){
            return Results.failure(ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR.code(), ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR.message());
        }
        return Results.success();
    }

    /**
     * 比赛列表分页查询
     * @param requestParam 分页查询参数
     * @return 分页查询返回集合
     */
    @GetMapping("/admin/all")
    public Result<IPage<ContestAllRespDTO>> all(ContestAllReqDTO requestParam){
        return Results.success(contestService.all(requestParam));
    }

    /**
     *
     */
    @GetMapping("/status")
    public Result<Void> changeStatus(ContestStatusReqDTO requestParam){
        contestService.changeStatus(requestParam);
        return Results.success();
    }
}
