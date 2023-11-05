package com.xclhove.xnote.exception;

import com.xclhove.xnote.enums.ResultStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xclhove
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ServiceException extends RuntimeException {
    private ResultStatus status;
    private String message;
}
