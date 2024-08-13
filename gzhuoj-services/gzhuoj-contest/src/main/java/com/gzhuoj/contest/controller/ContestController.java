package com.gzhuoj.contest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.constant.PathConstant;
import com.gzhuoj.contest.constant.PatternConstant;
import com.gzhuoj.contest.dto.req.contest.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestStatusReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestUpdateReqDTO;
import com.gzhuoj.contest.dto.resp.contest.ContestAllRespDTO;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.service.contest.ContestService;
import com.gzhuoj.contest.service.contest.UploadService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

import static common.convention.errorcode.BaseErrorCode.ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR;

/**
 * 比赛后端管理控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest/contest")
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
    @PutMapping("/update")
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
     * 更改比赛开放状态
     */
    @GetMapping("/status")
    public Result<Void> changeStatus(ContestStatusReqDTO requestParam){
        contestService.changeStatus(requestParam);
        return Results.success();
    }

    /**
     * 滚榜查看提交信息
     */
    @GetMapping("/submitData")
    public Result<List<SubmitDO> > submitData(Integer contestId){
        return Results.success(contestService.sumbitData(contestId));
    }

    /**
     *滚榜查看队伍信息
     */
    @GetMapping("/teamData")
    public Result<List<TeamDO> > teamData(Integer contestId){
        return Results.success(contestService.teamData(contestId));
    }
}
