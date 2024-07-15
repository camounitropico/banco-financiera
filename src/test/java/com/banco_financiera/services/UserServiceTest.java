package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.UserRepository;
import com.banco_financiera.requests.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveShouldReturnUserWhenUserIsOver18() throws HttpClientException {
        UserRequest userRequest = new UserRequest();
        userRequest.setBirthDate(LocalDate.now().minusYears(20));
        userRequest.setFirstName("John");
        userRequest.setIdentificationNumber(123456789L);
        userRequest.setIdentificationType("DNI");
        userRequest.setLastName("Doe");
        userRequest.setEmail("hello@iam.com");
        User user = new User();
        user.setId(123L);
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setFirstName("John");
        user.setIdentificationNumber(123456789L);
        user.setIdentificationType("DNI");
        user.setLastName("Doe");
        user.setEmail("hello@iam.com");
        when(userRepository.save(any())).thenReturn(user);

        User response = userService.save(userRequest);

        assertEquals(123L, response.getId());
    }

    @Test
    public void saveShouldThrowExceptionWhenUserIsUnder18() {
        UserRequest userRequest = new UserRequest();
        userRequest.setBirthDate(LocalDate.now().minusYears(17));

        assertThrows(HttpClientException.class, () -> userService.save(userRequest));
    }

    @Test
    public void upDateUserShouldReturnUpdatedUser() throws HttpClientException {
        UserRequest userRequest = new UserRequest();
        userRequest.setBirthDate(LocalDate.now().minusYears(20));
        userRequest.setFirstName("John");
        userRequest.setIdentificationNumber(123456789L);
        userRequest.setIdentificationType("DNI");
        userRequest.setLastName("Doe");
        userRequest.setEmail("hello@iam.com");
        User user = new User();
        user.setId(123L);
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setFirstName("John");
        user.setIdentificationNumber(123456789L);
        user.setIdentificationType("DNI");
        user.setLastName("Doe");
        user.setEmail("hello@iam.com");
        when(userRepository.save(user)).thenReturn(user);

        User response = userService.upDateUser(userRequest, user);

        assertEquals(123L, response.getId());
    }

    @Test
    public void upDateUserShouldThrowExceptionWhenUserIsUnder18() {
        UserRequest userRequest = new UserRequest();
        userRequest.setBirthDate(LocalDate.now().minusYears(17));
        User user = new User();
        user.setId(123L);

        assertThrows(HttpClientException.class, () -> userService.upDateUser(userRequest, user));
    }
}
