package com.banco_financiera.exception.service;

import lombok.Getter;

@Getter
public class TransactionProcessingException extends ServiceException {

    private final String transactionType;
    private final Long accountId;

    public TransactionProcessingException(String transactionType, Long accountId, String message) {
        super("TRANSACTION_PROCESSING_ERROR", message);
        this.transactionType = transactionType;
        this.accountId = accountId;
    }

    public TransactionProcessingException(String transactionType, Long accountId, String message, Throwable cause) {
        super("TRANSACTION_PROCESSING_ERROR", message, cause);
        this.transactionType = transactionType;
        this.accountId = accountId;
    }
}