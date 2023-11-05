package com.xclhove.xnote.Interceptor;

import com.xclhove.xnote.annotations.UserStatusIntercept;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.enums.UserStatus;
import com.xclhove.xnote.exception.UserStatusException;
import com.xclhove.xnote.service.impl.UserServiceImpl;
import com.xclhove.xnote.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xclhove
 */
public class UserStatusInterceptor implements HandlerInterceptor {
    @Autowired
    private UserServiceImpl userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UserStatusIntercept loginIntercept = handlerMethod.getMethod().getAnnotation(UserStatusIntercept.class);
        //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截，否则就需要进行拦截
        if(null == loginIntercept) {
            return true;
        }
        
        String token = request.getHeader("token");
        Integer id = TokenUtil.getId(token);
        User user = userService.getById(id);
        if (user.getStatus() == UserStatus.BANED) {
            throw new UserStatusException("用户已被禁封！");
        }
        return true;
    }
}
