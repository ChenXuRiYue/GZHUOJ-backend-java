package com.gzhuoj.usr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;

public interface PrivilegeService {
    IPage<AdminPrivilegeListRespDTO> privilegeList(AdminPrivilegeListReqDTO requestParam);
}
