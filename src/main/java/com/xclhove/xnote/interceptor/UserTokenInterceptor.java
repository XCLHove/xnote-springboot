package com.xclhove.xnote.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.constant.RequestHeaderKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.exception.UserTokenException;
import com.xclhove.xnote.pojo.enums.UserStatus;
import com.xclhove.xnote.pojo.table.User;
import com.xclhove.xnote.service.UserService;
import com.xclhove.xnote.tool.ThreadLocalTool;
import com.xclhove.xnote.tool.UserTokenTool;
import com.xclhove.xnote.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

/**
 * @author xclhove
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order
public class UserTokenInterceptor extends ServiceInterceptor {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserTokenIntercept {
        boolean needIntercept() default true;
    }
    
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        try {
            String token = request.getHeader(RequestHeaderKey.AUTHORIZATION);
            if (StrUtil.isBlank(token)) {
                throw new UserTokenException();
            }
            
            Integer userId = UserTokenTool.getUserId(token);
            if (userId == null) {
                throw new UserTokenException();
            }
            
            String deviceId = ThreadLocalUtil.get(TreadLocalKey.DEVICE_ID, String.class);
            String redisKey = userService.getTokenRedisKey(userId, deviceId);
            String tokenInRedis = stringRedisTemplate.opsForValue().get(redisKey);
            if (!token.equals(tokenInRedis)) {
                throw new UserTokenException();
            }
            
            User user = userService.getByIdWithRedis(userId);
            if (user == null) {
                throw new UserTokenException();
            }
            
            // 更新 token 过期时间
            stringRedisTemplate.expire(redisKey, Duration.ofHours(2));
            
            if (user.getStatus() == UserStatus.DISABLE) {
                throw new UserTokenException("账号已被禁用");
            }
            
            ThreadLocalTool.setUser(user);
            return true;
        } catch (UserTokenException userTokenException) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            UserTokenIntercept userTokenIntercept = handlerMethod.getMethod().getAnnotation(UserTokenIntercept.class);
            //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截
            if(userTokenIntercept == null) {
                return true;
            }
            if (!userTokenIntercept.needIntercept()) {
                return true;
            }
            throw userTokenException;
        }
    }
}