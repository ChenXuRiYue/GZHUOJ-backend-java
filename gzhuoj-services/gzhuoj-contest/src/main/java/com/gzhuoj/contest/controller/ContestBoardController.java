package com.gzhuoj.contest.controller;


import com.gzhuoj.contest.dto.req.board.BoardViewReq;
import com.gzhuoj.contest.model.pojo.PersonalScore;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-contest/board")
public class ContestBoardController {
//    @PostMapping("/show/{pageSize}/{goPage}")
//    public Result<List<PersonalScore>> boardViewWithLimit(@RequestBody BoardViewReq boardViewReq) {
//        return Results.success();
//    }
}
