package com.gzhuoj.usr.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.dto.req.admin.AdminChangeStatusReqDTO;
import com.gzhuoj.usr.dto.req.admin.AdminFileManagerReqDTO;
import com.gzhuoj.usr.dto.req.admin.AdminUserGenReqDTO;
import com.gzhuoj.usr.dto.req.admin.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.resp.admin.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.admin.AdminUserListRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;

import java.util.List;

public interface AdminService extends IService<UserDO> {

    IPage<AdminUserListRespDTO> userManagerList(AdminUserListReqDTO requestParam);

    void deleteUser(String userAccount);

    List<AdminUserGenRespDTO> userGen(AdminUserGenReqDTO requestParam);

    void changeStatus(AdminChangeStatusReqDTO requestParam);

    Void fileManager(AdminFileManagerReqDTO requestParam);
}
