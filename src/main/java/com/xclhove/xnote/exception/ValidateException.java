package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 参数校验异常
 *
 * @author xclhove
 */
public class ValidateException extends ServiceException {
    public ValidateException() {
        this(ResultType.VALIDATE_EXCEPTION.getMessage());
    }
    
    public ValidateException(String message) {
        super(ResultType.VALIDATE_EXCEPTION.getStatus(), message);
    }
}