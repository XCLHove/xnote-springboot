package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 请求路径不存在异常
 *
 * @author xclhove
 */
public class NotFoundException extends ServiceException {
    public NotFoundException() {
        this(ResultType.NOT_FOUND_EXCEPTION.getMessage());
    }
    
    public NotFoundException(String message) {
        this(message, null);
    }
    
    public NotFoundException(String message, Object data) {
        super(ResultType.NOT_FOUND_EXCEPTION.getStatus(), message, data);
    }
}