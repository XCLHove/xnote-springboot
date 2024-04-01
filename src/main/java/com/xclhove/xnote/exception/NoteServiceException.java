package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.result.ResultType;

/**
 * 笔记异常
 *
 * @author xclhove
 */
public class NoteServiceException extends ServiceException {
    public NoteServiceException() {
        this(ResultType.NOTE_SERVICE_EXCEPTION.getMessage());
    }
    
    public NoteServiceException(String message) {
        this(message, null);
    }
    
    public NoteServiceException(String message, Object data) {
        super(ResultType.NOTE_SERVICE_EXCEPTION.getStatus(), message, data);
    }
}