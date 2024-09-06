package com.xclhove.xnote.config;

import cn.hutool.core.util.StrUtil;
import com.xclhove.xnote.constant.RedisKey;
import com.xclhove.xnote.exception.AbstractServiceException;
import com.xclhove.xnote.exception.ParameterValidateException;
import com.xclhove.xnote.exception.SystemException;
import com.xclhove.xnote.tool.EmailTool;
import com.xclhove.xnote.tool.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 全局异常处理
 * ExceptionHandler相当于controller的@RequestMapping
 *
 * @author xclhove
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final EmailTool emailTool;
    private final JavaMailConfig javaMailConfig;
    private final StringRedisTemplate stringRedisTemplate;
    
    /**
     * 业务异常
     */
    @ExceptionHandler(AbstractServiceException.class)
    public Result<?> serviceExceptionHandle(HttpServletResponse response, AbstractServiceException exception) {
        response.setContentType("application/json");
        return Result.fail(exception.getStatus(), exception.getMessage(), exception.getData());
    }
    
    /**
     * 方法参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidExceptionHandler(HttpServletResponse response, MethodArgumentNotValidException exception) {
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        return serviceExceptionHandle(response, new ParameterValidateException(allErrors.get(0).getDefaultMessage()));
    }
    
    /**
     * 方法参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> methodArgumentTypeMismatchExceptionHandler(HttpServletResponse response, MethodArgumentTypeMismatchException exception) {
        return serviceExceptionHandle(response, new ParameterValidateException("参数类型不匹配：" + exception.getName()));
    }
    
    /**
     * 参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> constraintViolationExceptionHandler(HttpServletResponse response, ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).findFirst().orElse("");
        if (StrUtil.isBlank(message)) {
            return serviceExceptionHandle(response, new ParameterValidateException());
        }
        return serviceExceptionHandle(response, new ParameterValidateException(message));
    }
    
    /**
     * 缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> missingServletRequestParameterExceptionHandler(HttpServletResponse response, MissingServletRequestParameterException exception) {
        String message = "缺少请求参数：" + exception.getParameterName();
        return serviceExceptionHandle(response, new ParameterValidateException(message));
    }
    
    /**
     * JSON格式异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> jsonParseExceptionHandler(HttpServletResponse response, HttpMessageNotReadableException exception) {
        return serviceExceptionHandle(response, new SystemException("JSON格式错误"));
    }
    
    /**
     * 系统（未知）异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(HttpServletResponse response, Exception exception) {
        String message = String.join(":", "系统异常", exception.getMessage());
        
        String redisKey = RedisKey.Exception.CAN_SEND;
        Long expire = stringRedisTemplate.getExpire(redisKey);
        if (expire == null || expire == -1) {
            try {
                emailTool.sendMail(javaMailConfig.getUsername(), "XNote出现未知异常", message);
            } catch (MessagingException e) {
                log.error("邮件发送失败", e);
            }
            stringRedisTemplate.opsForValue().set(redisKey, "false", 30, TimeUnit.MINUTES);
        }
        
        log.error(message, exception);
        return serviceExceptionHandle(response, new SystemException());
    }
}
