package com.gzhuoj.usr.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.model.entity.UserDO;
import com.gzhuoj.usr.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Override
    public String test() {
        UserDO user = UserDO.builder()
                .email("11")
                .role(1)
                .password("111")
                .nickname("zzz")
                .orgnization("kk")
                .username("ljc")
                .build();
        baseMapper.insert(user);
        return "fuck";
    }
}
