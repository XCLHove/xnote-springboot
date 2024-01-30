package com.xclhove.xnote.Interceptor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xclhove.xnote.exception.IpFrequencyException;
import com.xclhove.xnote.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ip拦截器
 *
 * @author xclhove
 */
@Slf4j
@Component
public class IPInterceptor extends ServiceInterceptor {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UnlockIpFrequencyLimit {
    }
    
    private final Cache<String, AtomicInteger> ipCache = CacheBuilder.newBuilder()
            //写入过期时间为1分钟
            .expireAfterWrite(1, TimeUnit.MINUTES)
            //缓存最大数为300
            .maximumSize(300)
            .build();
    /** 单个ip每分钟最大请求次数 */
    @Value("${xnote.ip.max-frequency-per-minute}")
    private int maxFrequencyPerMinute;
    /** 单个ip访问频率异常提醒次数，如果超过提醒次数就不再返回任何信息 */
    final int promptNumber = 5;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果不是映射到方法
        if (!(handler instanceof HandlerMethod)) return true;
        
        //方法上有加这个注解，那么就说明这个方法不需要拦截
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UnlockIpFrequencyLimit unlockIpFrequencyLimit = handlerMethod.getMethod().getAnnotation(UnlockIpFrequencyLimit.class);
        if (null != unlockIpFrequencyLimit) return true;
        
        String ip = request.getRemoteAddr();
        int frequency = 0;
        
        try {
            frequency = ipCache.get(ip, () -> new AtomicInteger(0)).intValue();
            ipCache.put(ip, new AtomicInteger(++frequency));
        } catch (Exception e) {
            throw new IpFrequencyException("IP访问频率校验失败！");
        }
        if (frequency > (maxFrequencyPerMinute + promptNumber)) return false;
        if (frequency > maxFrequencyPerMinute) {
            String logMessage = "ip:" + ip;
            String token = request.getHeader("token");
            Integer id = TokenUtil.getId(token);
            if (id != null) logMessage += "--id:" + id;
            log.error(logMessage);
            throw new IpFrequencyException();
        }
        return true;
    }
}