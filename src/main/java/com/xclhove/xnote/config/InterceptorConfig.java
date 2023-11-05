package com.xclhove.xnote.config;

import com.xclhove.xnote.Interceptor.AdminJwtInterceptor;
import com.xclhove.xnote.Interceptor.UserJwtInterceptor;
import com.xclhove.xnote.Interceptor.UserStatusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xclhove
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截请求，通过判断token决定是否需要登录
        registry.addInterceptor(userJwtInterceptor())
                //.addPathPatterns("/users")
                //.addPathPatterns("/users/{userId}")
                //.addPathPatterns("/users/self")
                //.excludePathPatterns("/users/login")
                //.excludePathPatterns("/users/register")
                //.excludePathPatterns("/users/ban")
        ;
        //拦截请求，判断用户状态是否正常
        registry.addInterceptor(userStatusInterceptor())
                //.addPathPatterns("/users")
                //.addPathPatterns("/users/{userId}")
                //.addPathPatterns("/users/self")
        ;
        //拦截请求，通过判断admin_token决定是否需要管理员登录
        registry.addInterceptor(adminJwtInterceptor())
                //.addPathPatterns("/users/ban/**")
        ;
    }
    
    @Bean
    public UserJwtInterceptor userJwtInterceptor() {
        return new UserJwtInterceptor();
    }
    
    @Bean
    public UserStatusInterceptor userStatusInterceptor() {
        return new UserStatusInterceptor();
    }
    
    @Bean
    public AdminJwtInterceptor adminJwtInterceptor() {
        return new AdminJwtInterceptor();
    }
}
