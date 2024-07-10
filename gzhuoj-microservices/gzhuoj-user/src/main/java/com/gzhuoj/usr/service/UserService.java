package com.gzhuoj.usr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.model.entity.User;
import org.springframework.stereotype.Service;

public interface UserService extends IService<User> {
    String test();
}
