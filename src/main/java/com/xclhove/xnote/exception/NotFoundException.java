package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class NotFoundException extends AbstractServiceException {
    public NotFoundException() {
        this(ResultType.NOT_FOUND.getMessage());
    }
    
    public NotFoundException(String message) {
        this(message, null);
    }
    
    public NotFoundException(String message, Object data) {
        super(ResultType.NOT_FOUND.getStatus(), message, data);
    }
}