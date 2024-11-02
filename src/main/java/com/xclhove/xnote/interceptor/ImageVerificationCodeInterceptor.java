package com.xclhove.xnote.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.exception.NeedImageVerificationCodeException;
import com.xclhove.xnote.interceptor.annotations.NeedImageVerificationCode;
import com.xclhove.xnote.service.VerificationCodeService;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xclhove
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order
public class ImageVerificationCodeInterceptor extends ServiceInterceptor {
    
    
    private final VerificationCodeService verificationCodeService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NeedImageVerificationCode needImageVerificationCode = handlerMethod.getMethod().getAnnotation(NeedImageVerificationCode.class);
        // 如果方法上没有标注需要验证码，则直接通过
        if (needImageVerificationCode == null) {
            return true;
        }
        
        String imageVerificationCode = request.getParameter(needImageVerificationCode.parameterName());
        if (StrUtil.isBlank(imageVerificationCode)) {
            throw new NeedImageVerificationCodeException("图片验证码不能为空", needImageVerificationCode.parameterName());
        }
        
        if (!verificationCodeService.verifyImageVerificationCode(imageVerificationCode, ThreadLocalTool.getClientIpAddress())) {
            throw new NeedImageVerificationCodeException("图片验证码错误", needImageVerificationCode.parameterName());
        }
        
        return true;
    }
}