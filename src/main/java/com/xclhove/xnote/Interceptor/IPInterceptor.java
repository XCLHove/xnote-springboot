package com.xclhove.xnote.Interceptor;

import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.exception.IpFrequencyException;
import com.xclhove.xnote.tool.RedisTool;
import com.xclhove.xnote.util.RequestUtil;
import com.xclhove.xnote.util.ThreadLocalUtil;
import com.xclhove.xnote.util.TokenUtil;
import lombok.RequiredArgsConstructor;
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

/**
 * ip拦截器
 *
 * @author xclhove
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IPInterceptor extends ServiceInterceptor {
    private final RedisTool redisTool;
    @Value("${xnote.debug.enable: false}")
    private boolean isDebug;
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UnlockIpFrequencyLimit {
    }
    
    /** 单个ip每分钟最大请求次数 */
    @Value("${xnote.ip.max-frequency-per-minute}")
    private int maxFrequencyPerMinute;
    /** 单个ip访问频率异常提醒次数，如果超过提醒次数就不再返回任何信息 */
    final int promptNumber = 5;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = RequestUtil.getIpAddr(request);
        ThreadLocalUtil.set(TreadLocalKey.CLIENT_IP, ip);
        
        if (isDebug) return true;
        
        //如果不是映射到方法
        if (!(handler instanceof HandlerMethod)) return true;
        
        //方法上有加这个注解，那么就说明这个方法不需要拦截
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UnlockIpFrequencyLimit unlockIpFrequencyLimit = handlerMethod.getMethod().getAnnotation(UnlockIpFrequencyLimit.class);
        if (null != unlockIpFrequencyLimit) return true;
        
        int frequency = 0;
        
        Integer value = redisTool.getValue(RedisKey.IP_FREQUENCY + ip, Integer.class);
        frequency = (value == null || value < 0) ? 0 : value;
        
        redisTool.setValue(RedisKey.IP_FREQUENCY + ip, frequency + 1, 1, TimeUnit.MINUTES);
        
        if (frequency > (maxFrequencyPerMinute + promptNumber)) {
            return false;
        }
        if (frequency > maxFrequencyPerMinute) {
            String logMessage = "ip_limit:" + ip;
            String token = request.getHeader("token");
            Integer id = TokenUtil.getId(token);
            if (id != null) logMessage += "--id:" + id;
            log.error(logMessage);
            throw new IpFrequencyException();
        }
        return true;
    }
}