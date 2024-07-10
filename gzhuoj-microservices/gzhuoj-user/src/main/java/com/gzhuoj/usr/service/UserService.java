package com.gzhuoj.usr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.model.entity.UserDO;

public interface UserService extends IService<UserDO> {
    String test();
}
