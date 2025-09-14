package com.banco_financiera.exception.business;

import lombok.Getter;

@Getter
public class UserNotFoundException extends BusinessException {

    private final Long userId;
    private final Long identificationNumber;

    public UserNotFoundException(Long userId) {
        super("USER_NOT_FOUND",
              String.format("User with ID %d not found", userId));
        this.userId = userId;
        this.identificationNumber = null;
    }

    public UserNotFoundException(Long identificationNumber, boolean byIdentification) {
        super("USER_NOT_FOUND",
              String.format("User with identification number %d not found", identificationNumber));
        this.userId = null;
        this.identificationNumber = identificationNumber;
    }
}