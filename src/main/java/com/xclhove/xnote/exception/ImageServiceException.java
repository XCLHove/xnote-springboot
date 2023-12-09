package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 图片业务异常
 *
 * @author xclhove
 */
public class ImageServiceException extends ServiceException {
    public ImageServiceException() {
        this(ResultType.IMAGE_SERVICE_EXCEPTION.getMessage());
    }
    
    public ImageServiceException(String message) {
        super(ResultType.IMAGE_SERVICE_EXCEPTION.getStatus(), message);
    }
}