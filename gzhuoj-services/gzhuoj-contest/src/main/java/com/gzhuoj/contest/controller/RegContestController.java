package com.gzhuoj.contest.controller;

import com.gzhuoj.contest.dto.req.*;
import com.gzhuoj.contest.dto.resp.RegContestGenTeamRespDTO;
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

    @PostMapping("/admin/genteam")
    public Result<List<RegContestGenTeamRespDTO>> genTeam(@RequestBody RegContestGenTeamReqDTO requestParam){
        return Results.success(regContestService.genTeam(requestParam));
    }

    @PostMapping("/login")
    public Result<Void> login(@RequestBody RegContestLoginReqDTO requestParam){
        regContestService.login(requestParam);
        return Results.success();
    }

    @GetMapping("/logout")
    public Result<Void> logout(RegContestLogoutReqDTO requestParam){
        regContestService.logout(requestParam);
        return Results.success();
    }

    @GetMapping("/deleteteam")
    public Result<Void> deleteTeam(RegContestDelTeamReqDTO requestParam){
        regContestService.deleteTeam(requestParam);
        return Results.success();
    }

    @PostMapping("/updateteam")
    public Result<Void> updateTeam(@RequestBody RegContestUpdateTeamReqDTO requestParam){
        regContestService.updateTeam(requestParam);
        return Results.success();
    }
}
