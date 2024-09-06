package com.xclhove.xnote.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.constant.RequestHeaderKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.exception.DeviceFrequencyException;
import com.xclhove.xnote.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
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
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
@Order(0)
public class DeviceInterceptor extends ServiceInterceptor {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DeviceIntercept {
        /**
         * 设为false则不会进行拦截
         */
        boolean needIntercept() default true;
    }
    
    private final StringRedisTemplate stringRedisTemplate;
    private final XnoteConfig xnoteConfig;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userAgent = request.getHeader(RequestHeaderKey.USER_AGENT);
        String deviceId = SecureUtil.md5(userAgent);
        ThreadLocalUtil.set(TreadLocalKey.DEVICE_ID, deviceId);
        
        XnoteConfig.Interceptor.Device device = xnoteConfig.interceptor.device;
        if (device.getDisable()) {
            return true;
        }
        
        //如果不是映射到方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        DeviceIntercept intercept = handlerMethod.getMethod().getAnnotation(DeviceIntercept.class);
        if (intercept != null && !intercept.needIntercept()) {
            return true;
        }
        
        int crruentRequency = 0;
        String redisKey = RedisKey.join(RedisKey.Interceptor.DEVICE, deviceId);
        String lastFrequency = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isNotBlank(lastFrequency)) {
            crruentRequency = Integer.parseInt(lastFrequency);
        }
        crruentRequency++;
        if (crruentRequency > device.getMaxFrequencyPerMinute()) {
            throw new DeviceFrequencyException();
        }
        stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(crruentRequency), 1, TimeUnit.MINUTES);
        ThreadLocalUtil.set(TreadLocalKey.DEVICE_ID, deviceId);
        return true;
    }
}
