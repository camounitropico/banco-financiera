package com.banco_financiera.controllers;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.UserRequestDTO;
import com.banco_financiera.dto.UserResponseDTO;
import com.banco_financiera.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/banco-financiera/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "basicAuth")
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Get all users", description = "Retrieve all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.debug("Getting all users");
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by identification number", description = "Retrieve a specific user by their identification number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @GetMapping("/{identification_number}")
    public ResponseEntity<UserResponseDTO> getUserByIdentificationNumber(
            @Parameter(description = "User identification number", required = true)
            @PathVariable("identification_number") Long identificationNumber) {

        log.debug("Getting user by identification number: {}", identificationNumber);
        Optional<UserResponseDTO> user = userService.findByIdentificationNumber(identificationNumber);

        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new user", description = "Create a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Parameter(description = "User data", required = true)
            @Valid @RequestBody UserRequestDTO userRequestDTO) throws HttpClientException {

        log.debug("Creating user with identification number: {}", userRequestDTO.getIdentificationNumber());
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Update user", description = "Update an existing user by identification number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @PutMapping("/{identification_number}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User identification number", required = true)
            @PathVariable("identification_number") Long identificationNumber,
            @Parameter(description = "Updated user data", required = true)
            @Valid @RequestBody UserRequestDTO userRequestDTO) throws HttpClientException {

        log.debug("Updating user with identification number: {}", identificationNumber);
        UserResponseDTO updatedUser = userService.updateUser(identificationNumber, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Delete a user by identification number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @DeleteMapping("/{identification_number}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User identification number", required = true)
            @PathVariable("identification_number") Long identificationNumber) throws HttpClientException {

        log.debug("Deleting user with identification number: {}", identificationNumber);
        userService.deleteByIdentificationNumber(identificationNumber);
        return ResponseEntity.noContent().build();
    }
}