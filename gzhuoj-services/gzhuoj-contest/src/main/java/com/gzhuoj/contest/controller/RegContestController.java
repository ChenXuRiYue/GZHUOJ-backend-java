package com.gzhuoj.contest.controller;

import com.gzhuoj.contest.dto.req.RegContestGenTeamReqDTO;
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
}
