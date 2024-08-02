package com.gzhuoj.contest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.contest.dto.req.*;
import com.gzhuoj.contest.dto.resp.ContestWaitRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestStatusRespDTO;
import com.gzhuoj.contest.dto.resp.RegContestTeamInfoRespDTO;
import com.gzhuoj.contest.service.RegContestService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标准比赛控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/regular/contest")
public class RegContestController {
    private final RegContestService regContestService;

    /**
     * 批量生成比赛队伍
     * @param requestParam 生成队伍参数
     * @return 队伍实体集合
     */
    @PostMapping("/admin/genteam")
    public Result<List<RegContestGenTeamRespDTO>> genTeam(@RequestBody RegContestGenTeamReqDTO requestParam){
        return Results.success(regContestService.genTeam(requestParam));
    }

    /**
     * 队伍登录
     * @param requestParam 队伍登录参数
     */
    @PostMapping("/login")
    public Result<Void> login(@RequestBody RegContestLoginReqDTO requestParam){
        regContestService.login(requestParam);
        return Results.success();
    }

    /**
     * 队伍登出
     * @param requestParam 队伍登出参数
     */
    @GetMapping("/logout")
    public Result<Void> logout(RegContestLogoutReqDTO requestParam){
        regContestService.logout(requestParam);
        return Results.success();
    }

    /**
     * 队伍删除
     * @param requestParam 队伍删除参数
     */
    @GetMapping("/deleteteam")
    public Result<Void> deleteTeam(RegContestDelTeamReqDTO requestParam){
        regContestService.deleteTeam(requestParam);
        return Results.success();
    }

    /**
     * 队伍更新
     * @param requestParam 队伍更新参数
     */
    @PutMapping("/updateteam")
    public Result<Void> updateTeam(@RequestBody RegContestUpdateTeamReqDTO requestParam){
        regContestService.updateTeam(requestParam);
        return Results.success();
    }

    /**
     * 队伍信息查询
     * @param requestParam 查询参数
     */
    @GetMapping("/teaminfo")
    public Result<RegContestTeamInfoRespDTO> teamInfo(RegContestTeamInfoReqDTO requestParam){
        return Results.success(regContestService.teamInfo(requestParam));
    }

    @GetMapping("/status")
    public Result<IPage<RegContestStatusRespDTO>> status(RegContestStatusReqDTO requestParam){
        return Results.success(regContestService.status(requestParam));
    }


    @GetMapping("/wait")
    public Result<ContestWaitRespDTO> waitContest(ContestWaitReqDTO requestParam){
        return Results.success(regContestService.waitTime(requestParam));
    }
}
