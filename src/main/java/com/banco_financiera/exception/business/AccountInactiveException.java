package com.banco_financiera.exception.business;

import lombok.Getter;

@Getter
public class AccountInactiveException extends BusinessException {

    private final Long accountId;
    private final String currentStatus;

    public AccountInactiveException(Long accountId, String currentStatus) {
        super("ACCOUNT_INACTIVE",
              String.format("Account %d is %s and cannot perform transactions", accountId, currentStatus));
        this.accountId = accountId;
        this.currentStatus = currentStatus;
    }
}