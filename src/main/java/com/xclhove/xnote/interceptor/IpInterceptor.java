package com.xclhove.xnote.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.IpFrequencyException;
import com.xclhove.xnote.tool.ThreadLocalTool;
import com.xclhove.xnote.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
@RequiredArgsConstructor
public class IpInterceptor extends ServiceInterceptor {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PathFrequencyLimit {
        /**
         * 注解所在位置的请求路径的单个ip的每分钟最大请求次数
         */
        int maxFrequencyPerMinute() default 0;
        /**
         * 警告信息
         */
        String message() default "";
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DisableIpInterceptor {}
    
    private final StringRedisTemplate stringRedisTemplate;
    private final XnoteConfig xnoteConfig;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = RequestUtil.getClientIpAddress(request);
        ThreadLocalTool.setClientIp(clientIp);
        String requestPath = RequestUtil.getCurrentPath(request);
        
        //如果不是映射到方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 拦截特定请求路径
        PathFrequencyLimit pathFrequencyLimit = handlerMethod.getMethod().getAnnotation(PathFrequencyLimit.class);
        if (pathFrequencyLimit != null) {
            intercept(
                    RedisKey.join(clientIp, requestPath),
                    pathFrequencyLimit.maxFrequencyPerMinute(),
                    pathFrequencyLimit.message()
            );
        }
        
        XnoteConfig.Interceptor.Ip ip = xnoteConfig.interceptor.ip;
        if (ip.getDisable()) {
            return true;
        }
        DisableIpInterceptor disableIpInterceptor = handlerMethod.getMethod().getAnnotation(DisableIpInterceptor.class);
        if (disableIpInterceptor != null) {
            return true;
        }
        intercept(clientIp, ip.getMaxFrequencyPerMinute(), null);
        
        return true;
    }
    
    private void intercept(String key, int maxFrequencyPerMinute, String exceptionMessage) throws IpFrequencyException {
        if (maxFrequencyPerMinute <= 0) {
            return;
        }
        
        int crruentRequency = 0;
        String redisKey = RedisKey.join(RedisKey.Interceptor.IP, key);
        String lastFrequency = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isNotBlank(lastFrequency)) {
            crruentRequency = Integer.parseInt(lastFrequency);
        }
        crruentRequency++;
        stringRedisTemplate.opsForValue().increment(redisKey);
        if (crruentRequency <= maxFrequencyPerMinute) {
            stringRedisTemplate.opsForValue().set(redisKey, String.valueOf(crruentRequency), 1, TimeUnit.MINUTES);
            return;
        }
        if (StrUtil.isNotBlank(exceptionMessage)) {
            throw new IpFrequencyException(exceptionMessage);
        }
        throw new IpFrequencyException();
    }
}