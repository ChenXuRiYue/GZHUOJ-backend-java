package com.gzhuoj.usr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.service.AdminService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/userList")
    public Result<IPage<AdminUserListRespDTO>> userList(AdminUserListReqDTO requestParam){
        return Results.success(adminService.userList(requestParam));
    }

    /**
     * 用户权限分页查询
     * @param requestParam search -> 模糊查询字符串
     * @return 用户 账号 + 用户名 + 权限组成
     */
    @GetMapping("/privilege/list")
    public Result<IPage<AdminPrivilegeListRespDTO>> privilegeList(AdminPrivilegeListReqDTO requestParam){
        return Results.success(adminService.privilegeList(requestParam));
    }

}
