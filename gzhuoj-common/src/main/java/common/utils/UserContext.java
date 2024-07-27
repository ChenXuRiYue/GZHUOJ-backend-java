package common.utils;
public class UserContext {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到ThreadLocal
     * @param userId 用户id
     */
    public static void setUser(String userId) {
        threadLocal.set(userId);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户id
     */
    public static String getUser() {
        return threadLocal.get();
    }

    /**
     * 移除当前登录用户信息
     */
    public static void removeUser(){
        threadLocal.remove();
    }
}
