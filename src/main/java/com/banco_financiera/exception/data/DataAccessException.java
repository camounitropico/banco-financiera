package com.banco_financiera.exception.data;

import com.banco_financiera.exception.BaseException;

public abstract class DataAccessException extends BaseException {

    protected DataAccessException(String errorCode, String message) {
        super(errorCode, message, "DATA");
    }

    protected DataAccessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, "DATA", cause);
    }
}