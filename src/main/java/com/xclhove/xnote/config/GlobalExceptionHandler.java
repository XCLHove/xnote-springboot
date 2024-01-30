package com.xclhove.xnote.config;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.enums.result.ResultType;
import com.xclhove.xnote.exception.ParameterValidateException;
import com.xclhove.xnote.exception.ServiceException;
import com.xclhove.xnote.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常配置
 *
 * @author xclhove
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * ExceptionHandler相当于controller的@RequestMapping,如果抛出的的是ServiceException，则调用该方法
     *
     * @param exception 异常
     * @return 异常信息
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result<String> serviceHandle(HttpServletResponse response, ServiceException exception) {
        response.setContentType("application/json");
        return Result.error(exception.getStatus(), exception.getMessage());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result<String> constraintViolationHandle(HttpServletResponse response, ConstraintViolationException exception) {
        String message = StrUtil.isNotBlank(exception.getMessage()) ? exception.getMessage() : ResultType.PARAMETER_VALIDATE_EXCEPTION.getMessage();
        return serviceHandle(response, new ParameterValidateException(message));
    }
}
