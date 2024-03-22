package com.xclhove.xnote.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

/**
 * 子类查找器
 * @author xclhove
 */
public class SubclassFinder {
    
    public static Set<BeanDefinition> findSubclasses(Class<?> clazz) {
        return findSubclasses(clazz, "com.xclhove.xnote");
    }
    
    public static Set<BeanDefinition> findSubclasses(Class<?> clazz, String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(clazz));
        
        return provider.findCandidateComponents(basePackage);
    }
}