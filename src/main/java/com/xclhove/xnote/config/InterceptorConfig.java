package com.xclhove.xnote.config;

import com.xclhove.xnote.Interceptor.ServiceInterceptor;
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
        // 获取ServiceInterceptor的子类
        Set<BeanDefinition> subclasses = SubclassFinder.findSubclasses(ServiceInterceptor.class);
        
        // 将ServiceInterceptor的子类注册到拦截器
        subclasses.forEach(c -> {
            try {
                ServiceInterceptor interceptor = (ServiceInterceptor) applicationContext.getBean(Class.forName(c.getBeanClassName()));
                registry.addInterceptor(interceptor);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
