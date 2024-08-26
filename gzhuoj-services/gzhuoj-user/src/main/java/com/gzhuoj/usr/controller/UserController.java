package com.gzhuoj.usr.controller;

import com.gzhuoj.usr.dto.req.user.UserInfoUpdateReqDTO;
import com.gzhuoj.usr.dto.req.user.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.user.UserInfoRespDTO;
import com.gzhuoj.usr.dto.resp.user.UserInfoUpdateRespDTO;
import com.gzhuoj.usr.dto.resp.user.UserLoginRespDTO;
import com.gzhuoj.usr.service.user.UserService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-user/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam, HttpServletResponse response) {
        return Results.success(userService.login(requestParam, response));
    }

    /**
     * 用户信息更新
     * @param requestParam 用户信息, 每次更新时需要验证密码
     * @return 用户账号
     */
    @PutMapping("/update")
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
