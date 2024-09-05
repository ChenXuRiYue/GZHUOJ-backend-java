package common.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDO {
    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户角色, 为什么存入的是String?
     */
    private String role;
}
