package com.banco_financiera.exception.business;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends BusinessException {

    private final String resource;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String resource, String field, Object value) {
        super("DUPLICATE_RESOURCE",
              String.format("%s with %s '%s' already exists", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}