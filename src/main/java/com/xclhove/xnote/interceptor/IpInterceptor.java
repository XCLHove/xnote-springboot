package com.xclhove.xnote.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.exception.IpFrequencyException;
import com.xclhove.xnote.tool.ThreadLocalTool;
import com.xclhove.xnote.util.RequestUtil;
import com.xclhove.xnote.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;
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
        /**
         * 请求成功才计做一次,被任意拦截器拦截都不会计做一次
         */
        boolean needRequestSuccess() default false;
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
        
        // 是否禁用 IP 拦截器
        XnoteConfig.Interceptor.Ip ipInterceptorConfig = xnoteConfig.interceptor.ip;
        if (ipInterceptorConfig.getDisable()) {
            return true;
        }
        
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
                    pathFrequencyLimit.message(),
                    pathFrequencyLimit.needRequestSuccess()
            );
        }
        
        // 是否拦截
        DisableIpInterceptor disableIpInterceptor = handlerMethod.getMethod().getAnnotation(DisableIpInterceptor.class);
        if (disableIpInterceptor != null) {
            return true;
        }
        
        intercept(clientIp, ipInterceptorConfig.getMaxFrequencyPerMinute(), null, false);
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 更新 redis 中的访问频率
        Object setOrNull = ThreadLocalUtil.get(TreadLocalKey.THRADLOCAL_SET_REDIS_LIST_FOR_IP_INTERCEPTOR);
        Set<String> incrementRedisKeySet = new HashSet<>();
        if (setOrNull != null) {
            incrementRedisKeySet = (Set<String>) setOrNull;
        }
        
        incrementRedisKeySet.forEach(this::updateFrequency);
    }
    
    private void intercept(String key, int maxFrequencyPerMinute, String exceptionMessage, boolean needRequestSuccess) throws IpFrequencyException {
        if (maxFrequencyPerMinute <= 0) {
            return;
        }
        
        // 获取当前访问频率
        int crruentFrequency = 0;
        String redisKey = RedisKey.join(RedisKey.Interceptor.IP, key);
        String lastFrequency = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isNotBlank(lastFrequency)) {
            crruentFrequency = Integer.parseInt(lastFrequency);
        }
        crruentFrequency++;
        
        // 是否需要请求成功才计做一次
        if (needRequestSuccess) {
            updateFrequencyOnRequestSuccess(redisKey);
        } else {
            updateFrequency(redisKey);
        }
        
        // 未超过频率限制
        if (crruentFrequency <= maxFrequencyPerMinute) {
            return;
        }
        
        if (StrUtil.isNotBlank(exceptionMessage)) {
            throw new IpFrequencyException(exceptionMessage);
        }
        throw new IpFrequencyException();
    }
    
    /**
     * 立即更新 redis 中的访问频率
     */
    private void updateFrequency(String redisKey) {
        stringRedisTemplate.opsForValue().increment(redisKey);
        stringRedisTemplate.expire(redisKey, 1, TimeUnit.MINUTES);
    }
    
    /**
     * 请求成功后更新 redis 中的访问频率
     */
    private void updateFrequencyOnRequestSuccess(String redisKey) {
        // 将要更新的频率放入 ThreadLocal，等请求成功再去更新 redis 中的频率
        Object oldSetOrNull = ThreadLocalUtil.get(TreadLocalKey.THRADLOCAL_SET_REDIS_LIST_FOR_IP_INTERCEPTOR);
        Set<String> newIncrementRedisKeySet = new HashSet<>();
        if (oldSetOrNull != null) {
            newIncrementRedisKeySet = (Set<String>) oldSetOrNull;
        }
        newIncrementRedisKeySet.add(redisKey);
        ThreadLocalUtil.set(TreadLocalKey.THRADLOCAL_SET_REDIS_LIST_FOR_IP_INTERCEPTOR, newIncrementRedisKeySet);
    }
}