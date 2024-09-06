package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class UserImageServiceException extends AbstractServiceException {
    public UserImageServiceException() {
        this(ResultType.USER_IMAGE_SERVICE_EXCEPTION.getMessage());
    }
    
    public UserImageServiceException(String message) {
        this(message, null);
    }
    
    public UserImageServiceException(String message, Object data) {
        super(ResultType.USER_IMAGE_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}
