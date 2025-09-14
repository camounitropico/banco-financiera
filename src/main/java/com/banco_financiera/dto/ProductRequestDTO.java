package com.banco_financiera.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(savings|current)$", message = "Account type must be 'savings' or 'current'")
    @JsonProperty("account_type")
    private String accountType;

    @NotNull(message = "Account balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Account balance must be non-negative")
    @Digits(integer = 12, fraction = 2, message = "Account balance format is invalid")
    @JsonProperty("account_balance")
    private BigDecimal accountBalance;

    @NotNull(message = "Exempt GMF is required")
    @JsonProperty("exenta_gmf")
    private Boolean exemptGmf;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    @JsonProperty("user_id")
    private Long userId;
}