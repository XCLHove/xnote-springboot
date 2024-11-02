package com.xclhove.xnote.interceptor.annotations;

import com.xclhove.xnote.constant.ParameterName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xclhove
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedImageVerificationCode {
    String parameterName() default ParameterName.IMAGE_VERIFICATION_CODE;
}
