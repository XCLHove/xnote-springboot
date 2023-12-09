package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * IP 频率异常
 * @author xclhove
 */
public class IpFrequencyException extends ServiceException{
    public IpFrequencyException() {
        this(ResultType.IP_FREQUENCY_EXCEPTION.getMessage());
    }
    
    public IpFrequencyException(String message) {
        super(ResultType.IP_FREQUENCY_EXCEPTION.getStatus(), message);
    }
}