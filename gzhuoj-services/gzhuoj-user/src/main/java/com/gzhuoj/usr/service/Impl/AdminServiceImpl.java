package com.gzhuoj.usr.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.dto.req.*;
import com.gzhuoj.usr.dto.resp.AdminUserGenRespDTO;
import com.gzhuoj.usr.dto.resp.AdminUserListRespDTO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.remote.AdminRemoteService;
import com.gzhuoj.usr.remote.dto.req.UpdateContestReqDTO;
import com.gzhuoj.usr.remote.dto.req.UpdateProblemReqDTO;
import com.gzhuoj.usr.service.AdminService;
import common.toolkit.GenerateRandStrUtil;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends ServiceImpl<UserMapper, UserDO> implements AdminService {

    private final AdminRemoteService adminRemoteService;

    @Value("${batchImport.max_users}")
    private Integer MAX_USERS;

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
        // TODO 用户权限认证
        if (StrUtil.isEmpty(requestParam.getUserDescription())) {
            throw new ClientException("请输入至少一个用户描述和用户编号");
        }
        String userDescription = requestParam.getUserDescription();
        String[] split = userDescription.split("%0A");
        if (split.length > MAX_USERS) {
            throw new ClientException("一次最多生成" + MAX_USERS + "个用户");
        }
        List<AdminUserGenRespDTO> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() >= 3 && split[i].startsWith("%23")) {
                continue;
            }
            List<String> s = Arrays
                    .stream(split[i].split("%23"))
                    .map(each -> URLDecoder.decode(each).trim())
                    .collect(Collectors.toList());
            if (s.size() != 5) { // 五个字段
                continue;
            }
            AdminUserGenRespDTO adminUserGenRespDTO = new AdminUserGenRespDTO("", "", "", "", "");
            for (int j = 0; j < s.size(); j++) {
                if (StrUtil.isEmpty(s.get(j))) {
                    continue;
                }
                if (j == 0) adminUserGenRespDTO.setUserAccount(s.get(j));
                if (j == 1) adminUserGenRespDTO.setUsername(s.get(j));
                if (j == 2) adminUserGenRespDTO.setOrganization(s.get(j));
                if (j == 3) adminUserGenRespDTO.setEmail(s.get(j));
            }
            adminUserGenRespDTO.setPassword(GenerateRandStrUtil.getRandStr(8));
            // 将生成的用户插入到数据库
            UserDO userDO = BeanUtil.toBean(adminUserGenRespDTO, UserDO.class);
            userDO.setRole(2); // 默认为普通用户
            LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                    .eq(UserDO::getUserAccount, userDO.getUserAccount())
                    .eq(UserDO::getDeleteFlag, 0);
            UserDO hasUserDO = baseMapper.selectOne(queryWrapper);
            if(hasUserDO == null) {
                list.add(adminUserGenRespDTO);
            }
        }
        Collections.sort(list, Comparator.comparing(AdminUserGenRespDTO::getUserAccount));
        return list;
    }

    @Override
    public void changeStatus(AdminChangeStatusReqDTO requestParam) {
        if (Objects.equals("problem", requestParam.getItem())) {
            UpdateProblemReqDTO updateProblemReqDTO = new UpdateProblemReqDTO();
            updateProblemReqDTO.setProblemNum(requestParam.getId());
            updateProblemReqDTO.setProblemStatus(requestParam.getStatus() ^ 1);
            adminRemoteService.updateProblem(updateProblemReqDTO);
        } else if(Objects.equals("contest", requestParam.getItem())){
            UpdateContestReqDTO updateContestReqDTO = new UpdateContestReqDTO();
            updateContestReqDTO.setContestStatus(requestParam.getId());
            updateContestReqDTO.setContestStatus(requestParam.getStatus() ^ 1);
            adminRemoteService.updateContest(updateContestReqDTO);
        }
        /*
            TODO -> contest
                 -> article
        */
    }

    @Override
    public Void fileManager(AdminFileManagerReqDTO requestParam) {
        // TODO
        if (Objects.equals("problem", requestParam.getItem())) {
//            Wrappers.lambdaQuery()
        }
        return null;
        /*
            TODO -> contest
                 -> article
        */
    }

    public static void main(String[] args) {
        String[] s = {"aa", "ba", "bb"};
        Arrays.sort(s);
        System.out.println(Arrays.asList(s));
    }
}
