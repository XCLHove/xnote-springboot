package com.xclhove.xnote.Interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.xclhove.xnote.annotations.UserJwtIntercept;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.exception.UserLoginException;
import com.xclhove.xnote.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xclhove
 */
@Slf4j
public class UserJwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            //如果不是映射到方法直接通过
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            UserJwtIntercept loginIntercept = handlerMethod.getMethod().getAnnotation(UserJwtIntercept.class);
            //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截，否则就需要进行拦截
            if(null == loginIntercept) {
                return true;
            }
            
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                throw new UserLoginException("未登录！");
            }
            Integer id = TokenUtil.getId(token);
            User user = Db.getById(id, User.class);
            String password = user.getPassword();
            //校验token
            if (!TokenUtil.validate(token, password)) {
                throw new UserLoginException("token校验未通过！");
            }
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            throw new UserLoginException("token校验未通过！");
        }
    }
}