package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class DeviceFrequencyException extends AbstractServiceException {
    public DeviceFrequencyException() {
        this(ResultType.DEVICE_FREQUENCY_EXCEPTION.getMessage());
    }
    
    public DeviceFrequencyException(String message) {
        this(message, null);
    }
    
    public DeviceFrequencyException(String message, Object data) {
        super(ResultType.DEVICE_FREQUENCY_EXCEPTION.getStatus(), message, data);
    }
}