package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.dto.req.UserBatchImportReqDTO;
import com.gzhuoj.usr.dto.req.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.UserBatchImportRespDTO;
import com.gzhuoj.usr.dto.resp.UserLoginRespDTO;
import com.gzhuoj.usr.service.UserService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("user/test")
    public Result<String> test() {
        return Results.success(userService.test());
    }

    @GetMapping("/user/login")
    public Result<UserLoginRespDTO> login(UserLoginReqDTO requestParam) {
        return Results.success(userService.login(requestParam));
    }

    @GetMapping("/user/batchImport")
    public Result<ResponseEntity<byte[]>> batchImport(UserBatchImportReqDTO requestParam) throws IOException {
        return Results.success(userService.batchImport(requestParam));
    }

}
