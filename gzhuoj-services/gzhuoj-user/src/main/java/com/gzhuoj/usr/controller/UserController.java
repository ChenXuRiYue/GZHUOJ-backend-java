package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.service.UserService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("hello")
    private Result<Void> test(){
        userService.test();
        return Results.success();
    }
}
