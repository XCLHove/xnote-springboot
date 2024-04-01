package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 用户token异常
 *
 * @author xclhove
 */
public class UserTokenException extends ServiceException {
    public UserTokenException() {
        this(ResultType.USER_TOKEN_EXCEPTION.getMessage());
    }
    
    public UserTokenException(String message) {
        this(message, null);
    }
    
    public UserTokenException(String message, Object data) {
        super(ResultType.USER_TOKEN_EXCEPTION.getStatus(), message, data);
    }
}
