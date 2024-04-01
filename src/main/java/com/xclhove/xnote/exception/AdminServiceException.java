package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 管理员登录异常
 *
 * @author xclhove
 */
public class AdminServiceException extends ServiceException {
    public AdminServiceException() {
        this(ResultType.ADMIN_SERVICE_EXCEPTION.getMessage());
    }
    
    public AdminServiceException(String message) {
        this(message, null);
    }
    
    public AdminServiceException(String message, Object data) {
        super(ResultType.ADMIN_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}
