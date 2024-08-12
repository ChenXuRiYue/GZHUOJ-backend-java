package com.gzhuoj.usr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.privilege.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.resp.privilege.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.service.privilege.PrivilegeService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gzhuoj/admin")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    /**
     * 用户权限分页查询
     * @param requestParam search -> 模糊查询字符串
     * @return 用户 账号 + 用户名 + 权限组成
     */
    @GetMapping("/privilege/list")
    public Result<IPage<AdminPrivilegeListRespDTO>> privilegeList(AdminPrivilegeListReqDTO requestParam){
        return Results.success(privilegeService.privilegeList(requestParam));
    }
}
