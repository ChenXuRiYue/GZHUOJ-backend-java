package com.gzhuoj.usr.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.model.entity.User;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public String test() {
        User user = User.builder()
                .email("11")
                .role(1)
                .password("111")
                .updateTime(new Date())
                .createTime(new Date())
                .nickname("zzz")
                .orgnization("kk")
                .username("ljc")
                .deleteflag(0)
                .build();
        baseMapper.insert(user);
        return "fuck";
    }
}
