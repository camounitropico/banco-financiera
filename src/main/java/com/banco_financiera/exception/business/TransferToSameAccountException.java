package com.banco_financiera.exception.business;

import lombok.Getter;

@Getter
public class TransferToSameAccountException extends BusinessException {

    private final Long accountId;

    public TransferToSameAccountException(Long accountId) {
        super("TRANSFER_SAME_ACCOUNT",
              String.format("Cannot transfer to the same account: %d", accountId));
        this.accountId = accountId;
    }
}