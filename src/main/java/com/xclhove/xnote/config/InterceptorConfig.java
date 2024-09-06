package com.xclhove.xnote.config;

import com.xclhove.xnote.interceptor.ServiceInterceptor;
import com.xclhove.xnote.util.SubclassFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

/**
 * 拦截器配置
 *
 * @author xclhove
 */
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 获取 ServiceInterceptor 的子类拦截器
        Set<BeanDefinition> subclasses = SubclassFinder.findSubclasses(ServiceInterceptor.class);
        
        // 注册子类拦截器
        subclasses.forEach(beanDefinition -> {
            try {
                String className = beanDefinition.getBeanClassName();
                Class<ServiceInterceptor> intercepterClass = (Class<ServiceInterceptor>) Class.forName(className);
                ServiceInterceptor interceptor = applicationContext.getBean(intercepterClass);
                registry.addInterceptor(interceptor);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
