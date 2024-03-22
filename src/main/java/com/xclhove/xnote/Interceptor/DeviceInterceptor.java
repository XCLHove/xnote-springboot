package com.xclhove.xnote.Interceptor;

import cn.hutool.crypto.SecureUtil;
import com.xclhove.xnote.constant.RequestHeaderKey;
import com.xclhove.xnote.constant.TreadLocalKey;
import com.xclhove.xnote.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xclhove
 */
@Component
@RequiredArgsConstructor
@Order(0)
public class DeviceInterceptor extends ServiceInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgent = request.getHeader(RequestHeaderKey.USER_AGENT);
        String deviceId = SecureUtil.md5(userAgent);
        ThreadLocalUtil.set(TreadLocalKey.DEVICE_ID, deviceId);
        return true;
    }
}
