package com.banco_financiera.middlewares;

import com.banco_financiera.core.exceptions.HttpClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(HttpClientException error) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "ha sucedido algo extraño");
        body.put("cause", error.getCause() != null ? error.getCause().getLocalizedMessage() : "");

        return ResponseEntity
                .status(error.getStatus())
                .body(body);
    }
}
