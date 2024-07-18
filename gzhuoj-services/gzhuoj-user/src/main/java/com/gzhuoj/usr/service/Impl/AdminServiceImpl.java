package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.dto.req.AdminPrivilegeListReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserGenReqDTO;
import com.gzhuoj.usr.dto.req.AdminUserListReqDTO;
import com.gzhuoj.usr.dto.resp.AdminPrivilegeListRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.service.AdminService;
import com.gzhuoj.usr.utils.RoleUtil;
import com.gzhuoj.usr.utils.GenerateRandStrUtil;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        if (!StrUtil.isEmpty(requestParam.getSearch())) {
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

    @Override
    public List<AdminUserGenRespDTO> userGen(AdminUserGenReqDTO requestParam) {
        String userDescription = requestParam.getUserDescription();
        String[] split = userDescription.split("%0A");
        System.out.println(userDescription);
        System.out.println(Arrays.asList(split));
        List<AdminUserGenRespDTO> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() >= 3 && split[i].startsWith("%23")) {
                continue;
            }
            List<String> s = Arrays
                    .stream(split[i].split("%23"))
                    .map(each -> URLDecoder.decode(each).trim())
                    .collect(Collectors.toList());
            System.out.println(Arrays.asList(s));
            AdminUserGenRespDTO adminUserGenRespDTO = new AdminUserGenRespDTO("", "", "", "", "");
            for(int j = 0; j < s.size(); j++) {
                if(StrUtil.isEmpty(s.get(j))){
                    continue;
                }
                if(j == 0) adminUserGenRespDTO.setUserAccount(s.get(j));
                if(j == 1) adminUserGenRespDTO.setUsername(s.get(j));
                if(j == 2) adminUserGenRespDTO.setOrganization(s.get(j));
                if(j == 3) adminUserGenRespDTO.setEmail(s.get(j));
            }
            adminUserGenRespDTO.setPassword(GenerateRandStrUtil.getRandStr(8));
            list.add(adminUserGenRespDTO);
        }
        Collections.sort(list, Comparator.comparing(AdminUserGenRespDTO::getUserAccount));
        return list;
    }

    public static void main(String[] args) {
        String[] s= {"aa", "ba", "bb"};
        Arrays.sort(s);
        System.out.println(Arrays.asList(s));
    }
}
