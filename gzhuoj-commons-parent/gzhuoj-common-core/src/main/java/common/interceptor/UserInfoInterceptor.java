package common.interceptor;
import cn.hutool.core.util.StrUtil;
import common.biz.user.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的用户信息
        String userId = request.getHeader("userId");
        String role = request.getHeader("role");
        if(ObjectUtils.isEmpty(userId)) userId = "";
        // TODO
        if(ObjectUtils.isEmpty(role)) role = "0";
        UserInfoDO userInfoDO = new UserInfoDO(userId, role);
        // 2.判断是否为空
        UserContext.setUserInfo(userInfoDO);
        // 3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserContext.removeUser();
    }
}