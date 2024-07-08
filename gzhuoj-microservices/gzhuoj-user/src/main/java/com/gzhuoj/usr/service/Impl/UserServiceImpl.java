package com.gzhuoj.usr.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.usr.dao.entity.UserDO;
import com.gzhuoj.usr.mapper.UserMapper;
import com.gzhuoj.usr.service.UserService;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Override
    public void test() {
        Date date = new Date();
        UserDO userDO = UserDO.builder()
                .username("zzz")
                .password("123")
                .email("1234")
                .orgnization("dada")
                .createTime(date)
                .updateTime(date)
                .build();
        baseMapper.insert(userDO);
    }
}
