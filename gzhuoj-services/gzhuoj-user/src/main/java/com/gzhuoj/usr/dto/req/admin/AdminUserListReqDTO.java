package com.gzhuoj.usr.dto.req.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.usr.model.entity.UserDO;
import lombok.Data;

@Data
public class AdminUserListReqDTO extends Page<UserDO> {
    private String search;
}
