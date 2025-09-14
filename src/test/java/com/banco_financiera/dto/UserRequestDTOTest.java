package com.banco_financiera.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserRequestDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateValidUserRequestDTO() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setIdentificationType("CC");
        userRequestDTO.setIdentificationNumber(123456789L);
        userRequestDTO.setFirstName("John");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("john.doe@email.com");
        userRequestDTO.setBirthDate(LocalDate.now().minusYears(25));

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldFailValidationForBlankFirstName() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setIdentificationType("CC");
        userRequestDTO.setIdentificationNumber(123456789L);
        userRequestDTO.setFirstName(""); // Blank first name
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("john.doe@email.com");
        userRequestDTO.setBirthDate(LocalDate.now().minusYears(25));

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    public void shouldFailValidationForInvalidEmail() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setIdentificationType("CC");
        userRequestDTO.setIdentificationNumber(123456789L);
        userRequestDTO.setFirstName("John");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("invalid-email"); // Invalid email format
        userRequestDTO.setBirthDate(LocalDate.now().minusYears(25));

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void shouldSetAndGetAllFields() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        String identificationType = "CC";
        Long identificationNumber = 987654321L;
        String firstName = "Jane";
        String lastName = "Smith";
        String email = "jane.smith@email.com";
        LocalDate birthDate = LocalDate.of(1990, 5, 15);

        // Act
        userRequestDTO.setIdentificationType(identificationType);
        userRequestDTO.setIdentificationNumber(identificationNumber);
        userRequestDTO.setFirstName(firstName);
        userRequestDTO.setLastName(lastName);
        userRequestDTO.setEmail(email);
        userRequestDTO.setBirthDate(birthDate);

        // Assert
        assertEquals(identificationType, userRequestDTO.getIdentificationType());
        assertEquals(identificationNumber, userRequestDTO.getIdentificationNumber());
        assertEquals(firstName, userRequestDTO.getFirstName());
        assertEquals(lastName, userRequestDTO.getLastName());
        assertEquals(email, userRequestDTO.getEmail());
        assertEquals(birthDate, userRequestDTO.getBirthDate());
    }

    @Test
    public void shouldFailValidationForNullIdentificationNumber() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setIdentificationType("CC");
        userRequestDTO.setIdentificationNumber(null); // Null identification number
        userRequestDTO.setFirstName("John");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("john.doe@email.com");
        userRequestDTO.setBirthDate(LocalDate.now().minusYears(25));

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("identificationNumber")));
    }

    @Test
    public void shouldFailValidationForNullBirthDate() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setIdentificationType("CC");
        userRequestDTO.setIdentificationNumber(123456789L);
        userRequestDTO.setFirstName("John");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setEmail("john.doe@email.com");
        userRequestDTO.setBirthDate(null); // Null birth date

        // Act
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(userRequestDTO);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthDate")));
    }

    @Test
    public void shouldCreateInstanceWithNoArgsConstructor() {
        // Act
        UserRequestDTO userRequestDTO = new UserRequestDTO();

        // Assert
        assertNotNull(userRequestDTO);
        assertNull(userRequestDTO.getFirstName());
        assertNull(userRequestDTO.getLastName());
        assertNull(userRequestDTO.getEmail());
        assertNull(userRequestDTO.getIdentificationType());
        assertNull(userRequestDTO.getIdentificationNumber());
        assertNull(userRequestDTO.getBirthDate());
    }
}