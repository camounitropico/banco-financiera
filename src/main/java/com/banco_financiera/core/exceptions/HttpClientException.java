package com.banco_financiera.core.exceptions;

import org.springframework.http.HttpStatus;

public class HttpClientException extends Exception {
    private HttpStatus status;

    public HttpClientException(HttpStatus status, Throwable ex) {
        super(ex);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
