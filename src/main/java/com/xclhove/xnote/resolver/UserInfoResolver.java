package com.xclhove.xnote.resolver;

import com.xclhove.xnote.resolver.annotations.UserInfoFormToken;
import com.xclhove.xnote.tool.ThreadLocalTool;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author xclhove
 */
@Component
public class UserInfoResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInfoFormToken.class);
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        return ThreadLocalTool.getUser();
    }
    
    @Configuration
    @RequiredArgsConstructor
    public static class UserInfoResolverConfig implements WebMvcConfigurer {
        private final UserInfoResolver userInfoResolver;
        
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(userInfoResolver);
        }
    }
}
