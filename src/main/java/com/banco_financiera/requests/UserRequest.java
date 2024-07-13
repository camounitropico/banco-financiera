package com.banco_financiera.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    @JsonProperty("identification_type")
    private String identificationType;

    @JsonProperty("identification_number")
    private Long identificationNumber;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    @JsonProperty("birth_date")
    private LocalDate birthDate;
}
