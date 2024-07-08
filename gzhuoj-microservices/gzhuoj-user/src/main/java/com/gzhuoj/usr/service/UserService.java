package com.gzhuoj.usr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.dao.entity.UserDO;
import com.gzhuoj.usr.mapper.UserMapper;

public interface UserService extends IService<UserDO> {

    void test();
}
