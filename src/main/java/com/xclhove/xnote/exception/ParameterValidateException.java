package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 参数校验异常
 *
 * @author xclhove
 */
public class ParameterValidateException extends ServiceException {
    public ParameterValidateException() {
        this(ResultType.PARAMETER_VALIDATE_EXCEPTION.getMessage());
    }
    
    public ParameterValidateException(String message) {
        this(message, null);
    }
    
    public ParameterValidateException(String message, Object data) {
        super(ResultType.PARAMETER_VALIDATE_EXCEPTION.getStatus(), message, data);
    }
}