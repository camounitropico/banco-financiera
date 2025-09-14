package com.banco_financiera.exception;

import com.banco_financiera.dto.ErrorResponseDTO;
import com.banco_financiera.exception.business.*;
import com.banco_financiera.exception.data.DataAccessException;
import com.banco_financiera.exception.service.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation error on request to {}: {}", request.getRequestURI(), ex.getMessage());

        List<ErrorResponseDTO.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorResponseDTO.ValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()
                ))
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "VALIDATION_ERROR",
                "Request validation failed",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );
        errorResponse.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Business Layer Exceptions
    @ExceptionHandler({UserNotFoundException.class, AccountNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            BusinessException ex, HttpServletRequest request) {

        log.warn("Resource not found on request to {}: {} [Layer: {}]",
                request.getRequestURI(), ex.getMessage(), ex.getLayer());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientFunds(
            InsufficientFundsException ex, HttpServletRequest request) {

        log.warn("Insufficient funds on request to {}: Account={}, Available={}, Requested={}",
                request.getRequestURI(), ex.getAccountId(), ex.getCurrentBalance(), ex.getRequestedAmount());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({AccountInactiveException.class, TransferToSameAccountException.class})
    public ResponseEntity<ErrorResponseDTO> handleBusinessRuleViolation(
            BusinessException ex, HttpServletRequest request) {

        log.warn("Business rule violation on request to {}: {} [Layer: {}]",
                request.getRequestURI(), ex.getMessage(), ex.getLayer());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResource(
            DuplicateResourceException ex, HttpServletRequest request) {

        log.warn("Duplicate resource on request to {}: {}={} in {}",
                request.getRequestURI(), ex.getField(), ex.getValue(), ex.getResource());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Service Layer Exceptions
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponseDTO> handleServiceException(
            ServiceException ex, HttpServletRequest request) {

        log.error("Service error on request to {}: {} [Layer: {}]",
                 request.getRequestURI(), ex.getMessage(), ex.getLayer(), ex);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Data Layer Exceptions
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataAccessException(
            DataAccessException ex, HttpServletRequest request) {

        log.error("Data access error on request to {}: {} [Layer: {}]",
                 request.getRequestURI(), ex.getMessage(), ex.getLayer(), ex);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getErrorCode(),
                "Data access error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        log.warn("Data integrity violation on request to {}: {}", request.getRequestURI(), ex.getMessage());

        String message = "Data integrity constraint violated";
        String errorCode = "DATA_INTEGRITY_ERROR";

        if (ex.getMessage().contains("unique")) {
            message = "Resource already exists with provided data";
            errorCode = "DUPLICATE_RESOURCE";
        }

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                errorCode,
                message,
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("Illegal argument on request to {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "INVALID_ARGUMENT",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        log.error("Runtime error on request to {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "INTERNAL_ERROR",
                "An internal error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected error on request to {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "UNEXPECTED_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}