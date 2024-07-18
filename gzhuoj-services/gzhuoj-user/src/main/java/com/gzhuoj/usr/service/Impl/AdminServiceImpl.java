package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.service.AdminService;
import com.gzhuoj.usr.utils.RoleUtil;
import common.constant.RoleEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl extends ServiceImpl<UserMapper, UserDO> implements AdminService {
    @Override
    public IPage<AdminUserListRespDTO> userList(AdminUserListReqDTO requestParam) {
//        IPage<UserDO> resultPage = baseMapper.pageUser(requestParam);
//        return resultPage.convert(each -> {
//           AdminUserListRespDTO result = BeanUtil.toBean(each, AdminUserListRespDTO.class);
//           return result;
//        });
        return null;
    }

    @Override
    public IPage<AdminPrivilegeListRespDTO> privilegeList(AdminPrivilegeListReqDTO requestParam) {
        IPage<UserDO> resultPage = baseMapper.pageUserPrivilege(requestParam);
        return resultPage.convert(each -> {
            AdminPrivilegeListRespDTO bean = BeanUtil.toBean(each, AdminPrivilegeListRespDTO.class);
            bean.setRole(RoleUtil.getRoleName(each.getRole()));
            return bean;
        });
    }

}
