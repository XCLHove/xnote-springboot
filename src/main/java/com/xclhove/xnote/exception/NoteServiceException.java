package com.xclhove.xnote.exception;


import com.xclhove.xnote.enums.ResultType;

/**
 * @author xclhove
 */
public class NoteServiceException extends AbstractServiceException {
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
