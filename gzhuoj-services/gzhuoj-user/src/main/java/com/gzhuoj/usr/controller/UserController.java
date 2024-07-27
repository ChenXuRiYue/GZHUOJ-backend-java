package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.dto.req.UserBatchImportReqDTO;
import com.gzhuoj.usr.dto.req.UserInfoUpdateReqDTO;
import com.gzhuoj.usr.dto.req.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.UserBatchImportRespDTO;
import com.gzhuoj.usr.dto.resp.UserInfoRespDTO;
import com.gzhuoj.usr.dto.resp.UserInfoUpdateRespDTO;
import com.gzhuoj.usr.dto.resp.UserLoginRespDTO;
import com.gzhuoj.usr.service.UserService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(UserLoginReqDTO requestParam) {
        return Results.success(userService.login(requestParam));
    }

    /**
     * 用户信息更新
     * @param requestParam 用户信息, 每次更新时需要验证密码
     * @return 用户账号
     */
    @PostMapping("/update")
    public Result<UserInfoUpdateRespDTO> updateInfo(@RequestBody UserInfoUpdateReqDTO requestParam) {
        return Results.success(userService.updateInfo(requestParam));
    }

    /**
     * 根据用户账号返回用户信息
     * @param userAccount 用户账号
     * @return 用户信息
     */
    @GetMapping("/userinfo")
    public Result<UserInfoRespDTO> userInfo(@RequestParam String userAccount){
        return Results.success(userService.userInfo(userAccount));
    }
}
