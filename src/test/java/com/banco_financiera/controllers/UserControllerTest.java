package com.banco_financiera.controllers;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.User;
import com.banco_financiera.requests.UserRequest;
import com.banco_financiera.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnUserWhenUserExists() {
        User user = new User();
        user.setIdentificationNumber(123L);
        when(userService.findByIdentificationNumber(123L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(123L, response.getBody().getIdentificationNumber());
    }

    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExist() {
        when(userService.findByIdentificationNumber(123L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(123L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldCreateUserSuccessfully() throws HttpClientException {
        UserRequest userRequest = new UserRequest();
        userRequest.setIdentificationNumber(123L);
        User user = new User();
        user.setIdentificationNumber(123L);
        when(userService.save(userRequest)).thenReturn(user);

        User response = userController.createUser(userRequest);

        assertEquals(123L, response.getIdentificationNumber());
    }

    @Test
    public void shouldUpdateUserSuccessfully() throws HttpClientException {
        UserRequest userRequest = new UserRequest();
        userRequest.setIdentificationNumber(123L);
        User user = new User();
        user.setIdentificationNumber(123L);
        when(userService.findByIdentificationNumber(123L)).thenReturn(Optional.of(user));
        when(userService.upDateUser(userRequest, user)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(123L, userRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(123L, response.getBody().getIdentificationNumber());
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistingUser() throws HttpClientException {
        UserRequest userRequest = new UserRequest();
        userRequest.setIdentificationNumber(123L);
        when(userService.findByIdentificationNumber(123L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(123L, userRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldDeleteUserSuccessfully() {
        User user = new User();
        user.setIdentificationNumber(123L);
        when(userService.findByIdentificationNumber(123L)).thenReturn(Optional.of(user));

        ResponseEntity<Void> response = userController.deleteUser(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteById(user.getId());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistingUser() {
        when(userService.findByIdentificationNumber(123L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userController.deleteUser(123L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
