package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 管理员token异常
 *
 * @author xclhove
 */
public class AdminTokenException extends ServiceException {
    public AdminTokenException() {
        this(ResultType.ADMIN_TOKEN_EXCEPTION.getMessage());
    }
    
    public AdminTokenException(String message) {
        this(message, null);
    }
    
    public AdminTokenException(String message, Object data) {
        super(ResultType.ADMIN_TOKEN_EXCEPTION.getStatus(), message, data);
    }
}
