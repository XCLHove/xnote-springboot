package com.xclhove.xnote.interceptor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xclhove
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathFrequencyLimit {
    /**
     * 注解所在位置的请求路径的单个ip的每分钟最大请求次数
     */
    int maxFrequencyPerMinute() default 0;
    /**
     * 警告信息
     */
    String message() default "";
    /**
     * 请求成功才计做一次,被任意拦截器拦截都不会计做一次
     */
    boolean needRequestSuccess() default false;
}