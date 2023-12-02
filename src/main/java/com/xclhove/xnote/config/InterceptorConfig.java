package com.xclhove.xnote.config;

import com.xclhove.xnote.Interceptor.AdminJwtInterceptor;
import com.xclhove.xnote.Interceptor.UserJwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author xclhove
 */
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final UserJwtInterceptor userJwtInterceptor;
    private final AdminJwtInterceptor adminJwtInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userJwtInterceptor);
        registry.addInterceptor(adminJwtInterceptor);
    }
}
