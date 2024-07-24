package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.service.PrivilegeService;
import com.gzhuoj.usr.utils.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {

    private final UserMapper userMapper;

    @Override
    public IPage<AdminPrivilegeListRespDTO> privilegeList(AdminPrivilegeListReqDTO requestParam) {
        IPage<UserDO> resultPage = userMapper.pageUserPrivilege(requestParam);
        return resultPage.convert(each -> {
            AdminPrivilegeListRespDTO bean = BeanUtil.toBean(each, AdminPrivilegeListRespDTO.class);
            bean.setRole(RoleUtil.getRoleName(each.getRole()));
            return bean;
        });
    }
}
