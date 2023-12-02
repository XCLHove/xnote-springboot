package com.xclhove.xnote.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author xclhove
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ServiceException extends RuntimeException {
    private int status;
    private String message;
}
