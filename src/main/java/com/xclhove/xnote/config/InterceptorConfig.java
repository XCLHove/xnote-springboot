package com.xclhove.xnote.config;

import com.xclhove.xnote.interceptor.ServiceInterceptor;
import com.xclhove.xnote.util.SubclassFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 拦截器配置
 *
 * @author xclhove
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 获取 ServiceInterceptor 的子类拦截器
        Set<BeanDefinition> subclasses = SubclassFinder.findSubclasses(ServiceInterceptor.class);
        
        // 需要排序的拦截器
        List<ServiceInterceptor> needSortInterceptors = new ArrayList<>();
        
        // 无需排序的拦截器
        List<ServiceInterceptor> noSortInterceptors = new ArrayList<>();
        
        // 最末尾的拦截器
        List<ServiceInterceptor> lastInterceptors = new ArrayList<>();
        
        subclasses.forEach(beanDefinition -> {
            try {
                String className = beanDefinition.getBeanClassName();
                Class<ServiceInterceptor> intercepterClass = (Class<ServiceInterceptor>) Class.forName(className);
                Order order = intercepterClass.getAnnotation(Order.class);
                // 无需排序的拦截器
                if (order == null) {
                    noSortInterceptors.add(applicationContext.getBean(intercepterClass));
                    return;
                }
                
                // 最末尾的拦截器
                if (order.value() == Integer.MAX_VALUE) {
                    lastInterceptors.add(applicationContext.getBean(intercepterClass));
                    return;
                }
                
                // 需要排序的拦截器
                needSortInterceptors.add(applicationContext.getBean(intercepterClass));
            } catch (ClassNotFoundException e) {
                log.error("未找到拦截器：{}", e.getMessage());
            }
        });
        
        // 排序
        // needSortInterceptors.sort((o1, o2) -> o1.getClass().getAnnotation(Order.class).value() - o2.getClass().getAnnotation(Order.class).value());
        needSortInterceptors.sort(Comparator.comparingInt(o -> o.getClass().getAnnotation(Order.class).value()));
        
        // 注册拦截器
        needSortInterceptors.forEach(registry::addInterceptor);
        noSortInterceptors.forEach(registry::addInterceptor);
        lastInterceptors.forEach(registry::addInterceptor);
    }
}
