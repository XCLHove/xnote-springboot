package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 用户业务异常
 *
 * @author xclhove
 */
public class UserServiceException extends ServiceException {
    public UserServiceException() {
        this(ResultType.USER_SERVICE_EXCEPTION.getMessage());
    }
    
    public UserServiceException(String message) {
        this(message, null);
    }
    
    public UserServiceException(String message, Object data) {
        super(ResultType.USER_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}