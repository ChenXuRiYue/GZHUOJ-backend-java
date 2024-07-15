package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.service.AdminService;
import com.gzhuoj.usr.utils.RoleUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AdminServiceImpl extends ServiceImpl<UserMapper, UserDO> implements AdminService {

    @Override
    public IPage<AdminPrivilegeListRespDTO> privilegeList(AdminPrivilegeListReqDTO requestParam) {
        IPage<UserDO> resultPage = baseMapper.pageUserPrivilege(requestParam);
        return resultPage.convert(each -> {
            AdminPrivilegeListRespDTO bean = BeanUtil.toBean(each, AdminPrivilegeListRespDTO.class);
            bean.setRole(RoleUtil.getRoleName(each.getRole()));
            return bean;
        });
    }

    @Override
    public IPage<AdminUserListRespDTO> userManagerList(AdminUserListReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class);
        if(!StrUtil.isEmpty(requestParam.getSearch())){
            queryWrapper.like(UserDO::getUsername, requestParam.getSearch())
                    .or()
                    .like(UserDO::getUserAccount, requestParam.getSearch())
                    .or()
                    .like(UserDO::getOrganization, requestParam.getSearch());
        }
        IPage<UserDO> result = baseMapper.selectPage(requestParam, queryWrapper);
        AtomicInteger count = new AtomicInteger(1);
        return result.convert(each -> {
            AdminUserListRespDTO bean = BeanUtil.toBean(each, AdminUserListRespDTO.class);
            bean.setIdx(count.getAndIncrement());
            return bean;
        });
    }

    @Override
    public void deleteUser(String userAccount) {
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUserAccount, userAccount)
                .eq(UserDO::getDeleteFlag, 0);
        UserDO userDO = new UserDO();
        userDO.setDeleteFlag(1);
        baseMapper.update(userDO, updateWrapper);
    }

}
