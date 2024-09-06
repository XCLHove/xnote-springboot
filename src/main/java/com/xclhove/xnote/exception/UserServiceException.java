package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class UserServiceException extends AbstractServiceException {
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
