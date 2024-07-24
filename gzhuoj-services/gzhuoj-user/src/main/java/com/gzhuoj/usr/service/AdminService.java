package com.gzhuoj.usr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.dto.req.*;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;

import java.util.List;

public interface AdminService extends IService<UserDO> {

    IPage<AdminUserListRespDTO> userManagerList(AdminUserListReqDTO requestParam);

    void deleteUser(String userAccount);

    List<AdminUserGenRespDTO> userGen(AdminUserGenReqDTO requestParam);

    void changeStatus(AdminChangeStatusReqDTO requestParam);

    Void fileManager(AdminFileManagerReqDTO requestParam);
}
