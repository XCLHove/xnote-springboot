package com.xclhove.xnote.Interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.xclhove.xnote.constant.RequestHeaderKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.entity.table.User;
import com.xclhove.xnote.enums.entityattribute.UserStatus;
import com.xclhove.xnote.exception.ServiceException;
import com.xclhove.xnote.exception.UserTokenException;
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
public class UserJwtInterceptor extends ServiceInterceptor {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserJwtIntercept {
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
            UserJwtIntercept loginIntercept = handlerMethod.getMethod().getAnnotation(UserJwtIntercept.class);
            //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截，否则就需要进行拦截
            if(null == loginIntercept) {
                return true;
            }
            
            String token = request.getHeader(RequestHeaderKey.TOKEN);
            // token为空则未登录
            if (StrUtil.isBlank(token)) {
                throw new UserTokenException("未登录！");
            }
            
            Integer id = TokenUtil.getId(token);
            User user = Db.getById(id, User.class);
            // 检查是否被禁封
            if (user.getStatus() == UserStatus.BANED) {
                throw new UserTokenException("用户已被禁封！");
            }
            
            //校验token
            String password = user.getPassword();
            if (!TokenUtil.validate(token, password)) {
                throw new UserTokenException("token校验未通过！");
            }
            
            // 和redis中的token进行对比
            String tokenInRedis = tokenTool.get(user.getId());
            if ((StrUtil.isBlank(tokenInRedis)) || (!token.equals(tokenInRedis))) {
                throw new UserTokenException("登录过期，请重新登录！");
            }
            
            ThreadLocalUtil.set(TreadLocalKey.ID, id);
            return true;
        } catch (ServiceException serviceException) {
            throw serviceException;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new UserTokenException("出现异常，token校验未通过！");
        }
    }
}