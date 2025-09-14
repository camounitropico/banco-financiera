package com.banco_financiera.exception.business;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends BusinessException {

    private final Long accountId;

    public AccountNotFoundException(Long accountId) {
        super("ACCOUNT_NOT_FOUND",
              String.format("Account with ID %d not found", accountId));
        this.accountId = accountId;
    }
}