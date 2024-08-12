package com.gzhuoj.usr.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.dto.req.UserInfoUpdateReqDTO;
import com.gzhuoj.usr.dto.req.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.UserInfoRespDTO;
import com.gzhuoj.usr.dto.resp.UserInfoUpdateRespDTO;
import com.gzhuoj.usr.dto.resp.UserLoginRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;
import jakarta.servlet.http.HttpServletResponse;


public interface UserService extends IService<UserDO> {
    UserLoginRespDTO login(UserLoginReqDTO requestParam,  HttpServletResponse response);

    UserInfoUpdateRespDTO updateInfo(UserInfoUpdateReqDTO requestParam);

    UserInfoRespDTO userInfo(String userAccount);
}
