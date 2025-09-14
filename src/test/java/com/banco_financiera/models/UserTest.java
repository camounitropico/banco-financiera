package com.banco_financiera.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void shouldCreateUserWithAllFields() {
        // Arrange
        Long id = 1L;
        String identificationType = "CC";
        Long identificationNumber = 123456789L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@email.com";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        User user = new User();
        user.setId(id);
        user.setIdentificationType(identificationType);
        user.setIdentificationNumber(identificationNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(identificationType, user.getIdentificationType());
        assertEquals(identificationNumber, user.getIdentificationNumber());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    public void shouldCreateUserWithNoArgsConstructor() {
        // Act
        User user = new User();

        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getIdentificationType());
        assertNull(user.getIdentificationNumber());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    public void shouldSetAndGetId() {
        // Arrange
        User user = new User();
        Long expectedId = 100L;

        // Act
        user.setId(expectedId);

        // Assert
        assertEquals(expectedId, user.getId());
    }

    @Test
    public void shouldSetAndGetIdentificationDetails() {
        // Arrange
        User user = new User();
        String expectedType = "CE";
        Long expectedNumber = 987654321L;

        // Act
        user.setIdentificationType(expectedType);
        user.setIdentificationNumber(expectedNumber);

        // Assert
        assertEquals(expectedType, user.getIdentificationType());
        assertEquals(expectedNumber, user.getIdentificationNumber());
    }

    @Test
    public void shouldSetAndGetFullName() {
        // Arrange
        User user = new User();
        String expectedFirstName = "Jane";
        String expectedLastName = "Smith";

        // Act
        user.setFirstName(expectedFirstName);
        user.setLastName(expectedLastName);

        // Assert
        assertEquals(expectedFirstName, user.getFirstName());
        assertEquals(expectedLastName, user.getLastName());
    }

    @Test
    public void shouldSetAndGetEmail() {
        // Arrange
        User user = new User();
        String expectedEmail = "jane.smith@test.com";

        // Act
        user.setEmail(expectedEmail);

        // Assert
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    public void shouldSetAndGetTimestamps() {
        // Arrange
        User user = new User();
        LocalDateTime expectedCreatedAt = LocalDateTime.of(2023, 1, 1, 10, 0, 0);
        LocalDateTime expectedUpdatedAt = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

        // Act
        user.setCreatedAt(expectedCreatedAt);
        user.setUpdatedAt(expectedUpdatedAt);

        // Assert
        assertEquals(expectedCreatedAt, user.getCreatedAt());
        assertEquals(expectedUpdatedAt, user.getUpdatedAt());
    }

    @Test
    public void shouldHandleNullValues() {
        // Arrange
        User user = new User();

        // Act
        user.setId(null);
        user.setIdentificationType(null);
        user.setIdentificationNumber(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setEmail(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);

        // Assert
        assertNull(user.getId());
        assertNull(user.getIdentificationType());
        assertNull(user.getIdentificationNumber());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    public void shouldAllowUpdatingFields() {
        // Arrange
        User user = new User();
        user.setFirstName("Original");
        user.setEmail("original@email.com");

        // Act
        user.setFirstName("Updated");
        user.setEmail("updated@email.com");

        // Assert
        assertEquals("Updated", user.getFirstName());
        assertEquals("updated@email.com", user.getEmail());
    }

    @Test
    public void shouldHandleLongIdentificationNumbers() {
        // Arrange
        User user = new User();
        Long largeNumber = 99999999999L;

        // Act
        user.setIdentificationNumber(largeNumber);

        // Assert
        assertEquals(largeNumber, user.getIdentificationNumber());
    }
}