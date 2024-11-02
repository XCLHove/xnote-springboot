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
public @interface DeviceIntercept {
    /**
     * 设为false则不会进行拦截
     */
    boolean enable() default true;
}