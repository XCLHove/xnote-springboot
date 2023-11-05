package com.xclhove.xnote.config;

import com.xclhove.xnote.exception.ServiceException;
import com.xclhove.xnote.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param serviceException 异常
     * @return 异常信息
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result<String> serviceHandle(ServiceException serviceException) {
        return Result.error(serviceException.getStatus(),serviceException.getMessage());
    }
}
