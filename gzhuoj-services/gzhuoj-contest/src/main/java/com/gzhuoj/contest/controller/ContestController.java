package com.gzhuoj.contest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.constant.PathConstant;
import com.gzhuoj.contest.constant.PatternConstant;
import com.gzhuoj.contest.dto.ContestDTO;
import com.gzhuoj.contest.dto.req.contestProblem.ContestResultReqDTO;
import com.gzhuoj.contest.dto.resp.contest.PrintProblemRespDTO;
import com.gzhuoj.contest.dto.req.contest.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestReqDTO;
import com.gzhuoj.contest.dto.req.contest.ContestStatusReqDTO;
import com.gzhuoj.contest.dto.resp.contest.ContestAllRespDTO;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.entity.TeamDO;
import com.gzhuoj.contest.model.pojo.ContestProblemCalculation;
import com.gzhuoj.contest.service.contest.ContestService;
import com.gzhuoj.contest.service.contest.UploadService;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;
import java.util.regex.Pattern;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR;

/**
 * 比赛后端管理控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest/contest")
public class ContestController {
    private final ContestService contestService;
    private final ContestProblemService contestProblemService;
    private final UploadService uploadService;
    /**
     * 创建比赛
     * @param requestParam 创建比赛参数
     */
    @PostMapping("/create")
    public Result<Void> create(@RequestBody ContestReqDTO requestParam){
        contestService.createContest(requestParam);
        return Results.success();
    }

    /**
     * 更新比赛
     * @param requestParam 更新比赛参数
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody ContestReqDTO requestParam){
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
        return Results.success(contestService.getContestDO(num));
    }

    /**
     * 向用户提供基本的比赛视图信息, 只关注  contestDO 本身
     */
    @GetMapping("/query/basic-contest-info-for-user")
    public Result<ContestDTO> getContestBasicInfoForUser(Integer contestNum){
        return Results.success(contestService.getBasicInfoForContestProblemView(contestNum));
    }

    /**
     * 题目首页统计题目通过人数.
     * TODO 虽然写错了一点,但是可以留下来, 提交提交列表查询的地方可以用到.
     */
    @PostMapping("/get/contest/problem/calculation/basic")
    public Result<List<ContestProblemCalculation>> getContestProblemCalculation(ContestResultReqDTO contestResultReqDTO) {
        return Results.success(contestProblemService.getContestProblemCalculation(contestResultReqDTO));
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
    @PostMapping("/list")
    public Result<IPage<ContestAllRespDTO>> contestsView(@RequestBody ContestAllReqDTO requestParam){
        return Results.success(contestService.contestsView(requestParam));
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
    public Result<List<SubmitDO> > submitData(Integer contestNum){
        return Results.success(contestService.submitData(contestNum));
    }

    /**
     *滚榜查看队伍信息
     */
    @GetMapping("/teamData")
    public Result<List<TeamDO> > teamData(Integer contestNum){
        return Results.success(contestService.teamData(contestNum));
    }

    /**
     * 打印题目的接口
     */
    @GetMapping("/printProblem")
    public Result<PrintProblemRespDTO> printProblem(Integer contestNum){
        return Results.success(contestService.printProblem(contestNum));
    }

}
