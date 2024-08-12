package com.gzhuoj.usr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.privilege.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.model.entity.UserDO;


public interface UserMapper extends BaseMapper<UserDO> {
//    IPage<UserDO> pageUser(AdminUserListReqDTO requestParam);

    IPage<UserDO> pageUserPrivilege(AdminPrivilegeListReqDTO requestParam);
}
