package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.UserRequestDTO;
import com.banco_financiera.dto.UserResponseDTO;
import com.banco_financiera.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    /**
     * Retrieve all users
     * @return List of all users
     */
    List<UserResponseDTO> findAll();

    /**
     * Find user by ID
     * @param id User ID
     * @return Optional containing the user if found
     */
    Optional<UserResponseDTO> findById(Long id);

    /**
     * Find user by identification number
     * @param identificationNumber User identification number
     * @return Optional containing the user if found
     */
    Optional<UserResponseDTO> findByIdentificationNumber(Long identificationNumber);

    /**
     * Create a new user
     * @param userRequestDTO User data
     * @return Created user
     * @throws HttpClientException if validation fails or user creation fails
     */
    UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws HttpClientException;

    /**
     * Update an existing user
     * @param identificationNumber User identification number
     * @param userRequestDTO Updated user data
     * @return Updated user
     * @throws HttpClientException if user not found or update fails
     */
    UserResponseDTO updateUser(Long identificationNumber, UserRequestDTO userRequestDTO) throws HttpClientException;

    /**
     * Delete user by identification number
     * @param identificationNumber User identification number
     * @throws HttpClientException if user not found
     */
    void deleteByIdentificationNumber(Long identificationNumber) throws HttpClientException;

    /**
     * Check if user is over 18 years old
     * @param birthDate User birth date
     * @return true if user is 18 or older
     */
    boolean isUserOver18(LocalDate birthDate);

    /**
     * Get user entity by identification number (for internal use)
     * @param identificationNumber User identification number
     * @return User entity
     * @throws HttpClientException if user not found
     */
    User getUserEntityByIdentificationNumber(Long identificationNumber) throws HttpClientException;
}