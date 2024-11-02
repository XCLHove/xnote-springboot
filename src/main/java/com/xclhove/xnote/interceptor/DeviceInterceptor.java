package com.xclhove.xnote.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.config.XnoteConfig;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.exception.DeviceFrequencyException;
import com.xclhove.xnote.interceptor.annotations.DeviceIntercept;
import com.xclhove.xnote.util.RequestUtil;
import com.xclhove.xnote.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
@Order(0)
public class DeviceInterceptor extends ServiceInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    private final XnoteConfig xnoteConfig;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String deviceId = getDeviceId(request);
        
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
        if (intercept != null && !intercept.enable()) {
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
    
    public String getDeviceId(HttpServletRequest request) {
        String key = TreadLocalKey.DEVICE_ID;
        
        String deviceId = ThreadLocalUtil.get(key, String.class);
        if (StrUtil.isNotBlank(deviceId)) {
            return deviceId;
        }
        
        deviceId = RequestUtil.getDeviceIdFormUserAgent(request);
        ThreadLocalUtil.set(key, deviceId);
        
        return deviceId;
    }
}
