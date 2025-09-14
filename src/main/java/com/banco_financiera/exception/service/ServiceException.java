package com.banco_financiera.exception.service;

import com.banco_financiera.exception.BaseException;

public abstract class ServiceException extends BaseException {

    protected ServiceException(String errorCode, String message) {
        super(errorCode, message, "SERVICE");
    }

    protected ServiceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, "SERVICE", cause);
    }

    protected ServiceException(String errorCode, String message, Object details) {
        super(errorCode, message, "SERVICE", details);
    }
}