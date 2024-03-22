package com.xclhove.xnote.Interceptor.validator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.xclhove.xnote.Interceptor.ServiceInterceptor;
import com.xclhove.xnote.constant.RequestHeaderKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.enums.entityattribute.UserStatus;
import com.xclhove.xnote.tool.TokenTool;
import com.xclhove.xnote.util.ThreadLocalUtil;
import com.xclhove.xnote.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xclhove
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserJwtValidator extends ServiceInterceptor {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserJwtValidate {
    }
    
    private final TokenTool tokenTool;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            //如果不是映射到方法直接通过
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            UserJwtValidate loginIntercept = handlerMethod.getMethod().getAnnotation(UserJwtValidate.class);
            //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截，否则就需要进行拦截
            if(null == loginIntercept) {
                return true;
            }
            
            String token = request.getHeader(RequestHeaderKey.TOKEN);
            if (StrUtil.isBlank(token)) {
                ThreadLocalUtil.set(TreadLocalKey.ID, null);
                return true;
            }
            Integer id = TokenUtil.getId(token);
            User user = Db.getById(id, User.class);
            if (user.getStatus() == UserStatus.BANED) {
                ThreadLocalUtil.set(TreadLocalKey.ID, null);
                return true;
            }
            String tokenInRedis = tokenTool.get(user.getId());
            if ((StrUtil.isBlank(tokenInRedis)) || (!token.equals(tokenInRedis))) {
                ThreadLocalUtil.set(TreadLocalKey.ID, null);
                return true;
            }
            String password = user.getPassword();
            //校验token
            if (!TokenUtil.validate(token, password)) {
                ThreadLocalUtil.set(TreadLocalKey.ID, null);
                return true;
            }
            ThreadLocalUtil.set(TreadLocalKey.ID, id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            ThreadLocalUtil.set(TreadLocalKey.ID, null);
            return true;
        }
    }
}