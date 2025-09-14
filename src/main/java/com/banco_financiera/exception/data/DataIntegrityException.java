package com.banco_financiera.exception.data;

import lombok.Getter;

@Getter
public class DataIntegrityException extends DataAccessException {

    private final String constraintName;

    public DataIntegrityException(String message, String constraintName) {
        super("DATA_INTEGRITY_VIOLATION", message);
        this.constraintName = constraintName;
    }

    public DataIntegrityException(String message, String constraintName, Throwable cause) {
        super("DATA_INTEGRITY_VIOLATION", message, cause);
        this.constraintName = constraintName;
    }
}