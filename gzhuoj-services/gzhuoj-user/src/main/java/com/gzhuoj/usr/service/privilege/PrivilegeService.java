package com.gzhuoj.usr.service.privilege;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.privilege.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.resp.privilege.AdminPrivilegeListRespDTO;

public interface PrivilegeService {
    IPage<AdminPrivilegeListRespDTO> privilegeList(AdminPrivilegeListReqDTO requestParam);
}
