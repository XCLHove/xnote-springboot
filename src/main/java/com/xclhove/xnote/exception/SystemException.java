package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class SystemException extends AbstractServiceException {
    public SystemException() {
        this(ResultType.SYSTEM_EXCEPTION.getMessage());
    }
    
    public SystemException(String message) {
        this(message, null);
    }
    
    public SystemException(String message, Object data) {
        super(ResultType.SYSTEM_EXCEPTION.getStatus(), message, data);
    }
}
