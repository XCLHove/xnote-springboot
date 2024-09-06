package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class ImageServiceException extends AbstractServiceException {
    public ImageServiceException() {
        this(ResultType.IMAGE_SERVICE_EXCEPTION.getMessage());
    }
    
    public ImageServiceException(String message) {
        this(message, null);
    }
    
    public ImageServiceException(String message, Object data) {
        super(ResultType.IMAGE_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}
