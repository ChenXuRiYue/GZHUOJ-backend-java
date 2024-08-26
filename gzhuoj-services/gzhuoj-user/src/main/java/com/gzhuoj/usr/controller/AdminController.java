package com.gzhuoj.usr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.admin.AdminChangeStatusReqDTO;
import com.gzhuoj.usr.dto.req.admin.AdminFileManagerReqDTO;
import com.gzhuoj.usr.dto.req.admin.AdminUserGenReqDTO;
import com.gzhuoj.usr.dto.req.admin.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.req.user.UserReqDTO;
import com.gzhuoj.usr.dto.resp.admin.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.admin.AdminUserListRespDTO;
import com.gzhuoj.usr.service.admin.AdminService;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.gzhuoj.common.sdk.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-user/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 用户总表查询
     * @param requestParam 关键字查询
     * @return 用户分页查询返回数据
     */
    @GetMapping("/userManager/userList")
    public Result<IPage<AdminUserListRespDTO>> userManagerList(AdminUserListReqDTO requestParam){
        return Results.success(adminService.userManagerList(requestParam));
    }

    /**
     *
     * @param userReqDTO
     * @return
     */
    @PostMapping("/usermanager/delete")
    public Result<Void> userDelete(@RequestBody UserReqDTO userReqDTO){
        adminService.deleteUser(userReqDTO.getUserAccount());
        return Results.success();
    }

    /**
     * 用户批量生成
     * @param requestParam 用户数据转编的URL编码字符串
     * @return 生成的用户信息集合
     */
    @PostMapping("/usermanager/usergen")
    public Result<List<AdminUserGenRespDTO>> userGen(@RequestBody AdminUserGenReqDTO requestParam){
        return Results.success(adminService.userGen(requestParam));
    }
    // TODO 更友好的导入
//    @PostMapping("/usermanager/usergen/csv")
//    public Result<>


    /**
     * 修改事件状态 -> 题目、文章、比赛开发状态
     * @param requestParam 事件请求参数
     */
    @GetMapping("/changeStatus")
    public Result<Void> changeStatus(AdminChangeStatusReqDTO requestParam){
        adminService.changeStatus(requestParam);
        return Results.success();
    }


    @GetMapping("/filemanager")
    public Result<Void> fileManager(AdminFileManagerReqDTO requestParam){
        return Results.success(adminService.fileManager(requestParam));
    }

}
