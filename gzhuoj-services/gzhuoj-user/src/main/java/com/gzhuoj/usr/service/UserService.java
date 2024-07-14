package com.gzhuoj.usr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzhuoj.usr.dto.req.UserBatchImportReqDTO;
import com.gzhuoj.usr.dto.req.UserLoginReqDTO;
import com.gzhuoj.usr.dto.resp.UserLoginRespDTO;
import com.gzhuoj.usr.model.entity.UserDO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserService extends IService<UserDO> {
    String test();

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    ResponseEntity<byte[]> batchImport(UserBatchImportReqDTO requestParam) throws IOException;
}
