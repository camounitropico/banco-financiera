package com.banco_financiera.exception.business;

import com.banco_financiera.exception.BaseException;

public abstract class BusinessException extends BaseException {

    protected BusinessException(String errorCode, String message) {
        super(errorCode, message, "BUSINESS");
    }

    protected BusinessException(String errorCode, String message, Object details) {
        super(errorCode, message, "BUSINESS", details);
    }
}