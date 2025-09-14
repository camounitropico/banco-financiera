package com.banco_financiera.exception.business;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientFundsException extends BusinessException {

    private final Long accountId;
    private final BigDecimal currentBalance;
    private final BigDecimal requestedAmount;

    public InsufficientFundsException(Long accountId, BigDecimal currentBalance, BigDecimal requestedAmount) {
        super("INSUFFICIENT_FUNDS",
              String.format("Insufficient funds in account %d. Available: %s, Requested: %s",
                          accountId, currentBalance, requestedAmount));
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }
}