package com.gzhuoj.contest.controller;

import com.gzhuoj.contest.dto.req.contestBalloon.ContestBalloonChangeStReqDTO;
import com.gzhuoj.contest.dto.req.contestBalloon.ContestBalloonQueueReqDTO;
import com.gzhuoj.contest.dto.resp.contestBalloon.ContestBalloonQueueRespDTO;
import com.gzhuoj.contest.service.contestBalloon.ContestBalloonService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest/balloon")
public class ContestBalloonController {
    private final ContestBalloonService contestBalloonService;

    /**
     * 气球队列 -- 目前并特殊的气球配送员，直接让admin用户进行配发
     * @param requestParam 根据比赛id和room查看气球任务
     * @return 气球分配队列
     */
    @GetMapping("/queue")
    public Result<List<ContestBalloonQueueRespDTO>> queue(ContestBalloonQueueReqDTO requestParam){
        return Results.success(contestBalloonService.queue(requestParam));
    }

    @PostMapping("/status")
    private Result<Void> status(@RequestBody ContestBalloonChangeStReqDTO requestParam){
        contestBalloonService.status(requestParam);
        return Results.success();
    }
}
