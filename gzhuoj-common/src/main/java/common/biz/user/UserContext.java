package common.biz.user;

import common.interceptor.UserInfoDO;

public final class UserContext {
    private static final ThreadLocal<UserInfoDO> threadLocal = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到ThreadLocal
     * @param userInfo 用户上下文实体
     */
    public static void setUserInfo(UserInfoDO userInfo) { threadLocal.set(userInfo);};

    /**
     * 获取当前登录用户Id
     * @return 用户id
     */
    public static String getUserId() {
        return threadLocal.get().getUserId();
    }

    /**
     * 获取当前登录用户角色
     * @return 用户角色
     */
    public static String getRole() {
        return threadLocal.get().getRole();
    }

    /**
     * 移除当前登录用户信息
     */
    public static void removeUser(){
        threadLocal.remove();
    }
}
