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
        super(ResultType.USER_TOKEN_EXCEPTION.getStatus(), message);
    }
}