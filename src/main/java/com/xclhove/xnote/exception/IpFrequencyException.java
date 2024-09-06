package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class IpFrequencyException extends AbstractServiceException {
    public IpFrequencyException() {
        this(ResultType.IP_FREQUENCY_EXCEPTION.getMessage());
    }
    
    public IpFrequencyException(String message) {
        this(message, null);
    }
    
    public IpFrequencyException(String message, Object data) {
        super(ResultType.IP_FREQUENCY_EXCEPTION.getStatus(), message, data);
    }
}