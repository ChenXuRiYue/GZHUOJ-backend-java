package com.gzhuoj.contest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.dto.req.contest.ContestUpdateInfoPushReqDTO;
import com.gzhuoj.contest.dto.req.regContest.*;
import com.gzhuoj.contest.dto.resp.regContest.*;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.service.regContest.RegContestService;
import common.Handler.WebSocketMessageHandler;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.gzhuoj.common.sdk.model.pojo.Options;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标准比赛控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest/regular/contest")
public class RegContestController {
    private final RegContestService regContestService;

    /**
     * 批量生成比赛队伍
     *
     * @param requestParam 生成队伍参数
     * @return 队伍实体集合
     */
    @PostMapping("/admin/genteam")
    public Result<List<RegContestGenTeamRespDTO>> genTeam(@RequestBody RegContestGenTeamReqDTO requestParam) {
        return Results.success(regContestService.genTeam(requestParam));
    }

    /**
     * 队伍登录
     *
     * @param requestParam 队伍登录参数
     */
    @PostMapping("/login")
    public Result<RegContestLoginRespDTO> login(@RequestBody RegContestLoginReqDTO requestParam, HttpServletResponse response) {
        return Results.success(regContestService.login(requestParam, response));
    }

    /**
     * 队伍登出
     *
     * @param requestParam 队伍登出参数
     */
    @GetMapping("/logout")
    public Result<Void> logout(RegContestLogoutReqDTO requestParam) {
        regContestService.logout(requestParam);
        return Results.success();
    }

    /**
     * 队伍删除
     *
     * @param requestParam 队伍删除参数
     */
    @GetMapping("/deleteteam")
    public Result<Void> deleteTeam(RegContestDelTeamReqDTO requestParam) {
        regContestService.deleteTeam(requestParam);
        return Results.success();
    }

    /**
     * 队伍更新
     *
     * @param requestParam 队伍更新参数
     */
    @PutMapping("/updateteam")
    public Result<Void> updateTeam(@RequestBody RegContestUpdateTeamReqDTO requestParam) {
        regContestService.updateTeam(requestParam);
        return Results.success();
    }

    /**
     * 队伍信息查询
     *
     * @param requestParam 查询参数
     */
    @GetMapping("/teaminfo")
    public Result<RegContestTeamInfoRespDTO> teamInfo(RegContestTeamInfoReqDTO requestParam) {
        return Results.success(regContestService.teamInfo(requestParam));
    }

    /**
     * 评测结果展示
     *
     * @param requestParam 评测结果查询参数
     * @return 评测结果分页查询
     */
    @GetMapping("/status")
    public Result<IPage<RegContestStatusRespDTO>> status(RegContestStatusReqDTO requestParam) {
        return Results.success(regContestService.status(requestParam));
    }

    /**
     * 根据比赛id查询比赛是否存在
     *
     * @param contestNum 比赛id
     * @return 存在返回 true
     */
    @GetMapping("/exist")
    public Result<ContestDO> getContest(Integer contestNum) {
        return Results.success(regContestService.getContest(contestNum));
    }

    @PostMapping("/notification")
    public Result<Void> notifiToAll(@RequestBody RegContestNotifiReqDTO requestParam){
        regContestService.notifiToAll(requestParam);
        return Results.success();
    }
    /**
     * 比赛题目集界面
     *
     * @param requestParam 比赛题目集界面入参
     * @return 题目列表展示返回实体
     */
    @PostMapping("/problems")
    public Result<List<RegContestProblemRespDTO>> problemSet(@RequestBody RegContestProSetReqDTO requestParam) {
        return Results.success(regContestService.getContestProblemSetView(requestParam));
    }


    @PostMapping("/update")
    public Result<Void> pushContestUpdate(@RequestBody ContestUpdateInfoPushReqDTO requestParam){
        regContestService.pushContestUpdate(requestParam);
        return Results.success();
    }

    @GetMapping("/wait")
    public Result<ContestWaitRespDTO> waitContest(ContestWaitReqDTO requestParam) {
        return Results.success(regContestService.waitTime(requestParam));
    }

    @PostMapping("/seat")
    public Result<ContestSeatRespDTO> contestSeat(Integer contestNum,@RequestBody ContestSeatReqDTO reqDTO){
        return Results.success(regContestService.contestSeat(contestNum,reqDTO));
    }

    @GetMapping("/options/language")
    public Result<Options<String, Integer>> getLanguageOptions(Integer contestNum) {
        return Results.success(regContestService.getLanguageOptions(contestNum));
    }

    @GetMapping("/options/problem")
    public Result<Options<String, Integer>> getProblemOptions(Integer contestNum) {
        return Results.success(regContestService.getProblemOptions(contestNum));
    }
}
