package com.banco_financiera.exception.data;

public class DatabaseConnectionException extends DataAccessException {

    public DatabaseConnectionException(String message, Throwable cause) {
        super("DATABASE_CONNECTION_ERROR", message, cause);
    }
}