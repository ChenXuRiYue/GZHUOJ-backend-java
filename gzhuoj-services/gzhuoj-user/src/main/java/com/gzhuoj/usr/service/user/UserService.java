package com.gzhuoj.usr.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.dto.req.user.UserInfoUpdateReqDTO;
import com.gzhuoj.usr.dto.req.user.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.user.UserInfoRespDTO;
import com.gzhuoj.usr.dto.resp.user.UserInfoUpdateRespDTO;
import com.gzhuoj.usr.dto.resp.user.UserLoginRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService extends IService<UserDO> {
    UserLoginRespDTO login(UserLoginReqDTO requestParam,  HttpServletResponse response);

    UserInfoUpdateRespDTO updateInfo(UserInfoUpdateReqDTO requestParam);

    UserInfoRespDTO userInfo(String userAccount);
}
