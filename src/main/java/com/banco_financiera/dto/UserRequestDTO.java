package com.banco_financiera.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Identification type is required")
    @Pattern(regexp = "^(CC|CE|PA|TI)$", message = "Identification type must be CC, CE, PA, or TI")
    @JsonProperty("identification_type")
    private String identificationType;

    @NotNull(message = "Identification number is required")
    @Positive(message = "Identification number must be positive")
    @JsonProperty("identification_number")
    private Long identificationNumber;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @JsonProperty("birth_date")
    private LocalDate birthDate;
}