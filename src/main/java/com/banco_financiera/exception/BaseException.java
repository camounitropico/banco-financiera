package com.banco_financiera.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final String layer;
    private final Object details;

    protected BaseException(String errorCode, String message, String layer) {
        super(message);
        this.errorCode = errorCode;
        this.layer = layer;
        this.details = null;
    }

    protected BaseException(String errorCode, String message, String layer, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.layer = layer;
        this.details = details;
    }

    protected BaseException(String errorCode, String message, String layer, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.layer = layer;
        this.details = null;
    }
}