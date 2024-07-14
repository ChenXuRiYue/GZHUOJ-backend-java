package common.constant;

import cn.hutool.core.lang.Editor;
import cn.hutool.http.useragent.Browser;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum RoleEnum {

    /**
     * Regular User
     */
    REGULAR_USER(0, "普通用户"),

    /**
     * Admin
     */
    ADMIN(1, "管理员"),

    /**
     * Super Admin
     */
    SUPER_ADMIN(2, "超级管理员");
    RoleEnum(Integer role, String privilege){
        this.role = role;
        this.privilege = privilege;
    }
    @Getter
    private final Integer role;
    @Getter
    private final String privilege;
}
