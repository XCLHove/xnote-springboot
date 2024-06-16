package com.xclhove.xnote.runner;

import com.xclhove.xnote.config.RunnerConfig;
import com.xclhove.xnote.util.SubclassFinder;
import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author xclhove
 */
@Component
@Data
public abstract class AbstractRunner implements ApplicationRunner {
    @Resource
    private final ApplicationContext applicationContext;
    @Resource
    protected RunnerConfig runnerConfig;
    
    @Override
    public void run(ApplicationArguments args) {
        // 获取AbstractRunner的子类
        Set<BeanDefinition> subclasses = SubclassFinder.findSubclasses(AbstractRunner.class);
        
        // 执行AbstractRunner子类的doRun方法
        subclasses.forEach(beanDefinition -> {
            try {
                Class<?> aClass = Class.forName(beanDefinition.getBeanClassName());
                AbstractRunner runner = (AbstractRunner) applicationContext.getBean(aClass);
                runner.doRun(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public abstract void doRun(ApplicationArguments args);
}
