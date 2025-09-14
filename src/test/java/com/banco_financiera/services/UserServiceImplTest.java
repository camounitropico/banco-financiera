package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.UserRequestDTO;
import com.banco_financiera.dto.UserResponseDTO;
import com.banco_financiera.exception.business.UserNotFoundException;
import com.banco_financiera.exception.business.DuplicateResourceException;
import com.banco_financiera.mapper.UserMapper;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    public void createUserShouldReturnUserWhenUserIsOver18() throws HttpClientException {
        // Arrange
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setBirthDate(LocalDate.now().minusYears(20));
        userRequest.setFirstName("John");
        userRequest.setIdentificationNumber(123456789L);
        userRequest.setIdentificationType("CC");
        userRequest.setLastName("Doe");
        userRequest.setEmail("john.doe@email.com");

        User user = new User();
        user.setId(1L);
        user.setIdentificationNumber(123456789L);

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setIdentificationNumber(123456789L);
        userResponse.setFirstName("John");
        userResponse.setLastName("Doe");

        when(userRepository.findByIdentificationNumber(123456789L)).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        // Act
        UserResponseDTO response = userService.createUser(userRequest);

        // Assert
        assertNotNull(response);
        assertEquals(123456789L, response.getIdentificationNumber());
        assertEquals("John", response.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void createUserShouldThrowExceptionWhenUserIsUnder18() {
        // Arrange
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setBirthDate(LocalDate.now().minusYears(17));
        userRequest.setFirstName("Jane");
        userRequest.setLastName("Doe");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void findAllShouldReturnAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");

        UserResponseDTO userResponse1 = new UserResponseDTO();
        userResponse1.setFirstName("John");

        UserResponseDTO userResponse2 = new UserResponseDTO();
        userResponse2.setFirstName("Jane");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toResponseDTOList(Arrays.asList(user1, user2))).thenReturn(Arrays.asList(userResponse1, userResponse2));

        // Act
        List<UserResponseDTO> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    public void findByIdentificationNumberShouldReturnUserWhenExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setIdentificationNumber(123456789L);

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setIdentificationNumber(123456789L);

        when(userRepository.findByIdentificationNumber(123456789L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        // Act
        Optional<UserResponseDTO> result = userService.findByIdentificationNumber(123456789L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(123456789L, result.get().getIdentificationNumber());
    }

    @Test
    public void findByIdentificationNumberShouldReturnEmptyWhenNotExists() {
        // Arrange
        when(userRepository.findByIdentificationNumber(123456789L)).thenReturn(Optional.empty());

        // Act
        Optional<UserResponseDTO> result = userService.findByIdentificationNumber(123456789L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void updateUserShouldReturnUpdatedUser() throws HttpClientException {
        // Arrange
        Long identificationNumber = 123456789L;
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setIdentificationNumber(identificationNumber);
        userRequest.setBirthDate(LocalDate.now().minusYears(25));
        userRequest.setFirstName("John Updated");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setIdentificationNumber(identificationNumber);
        existingUser.setFirstName("John");

        UserResponseDTO updatedUserResponse = new UserResponseDTO();
        updatedUserResponse.setIdentificationNumber(identificationNumber);
        updatedUserResponse.setFirstName("John Updated");

        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(updatedUserResponse);

        // Act
        UserResponseDTO result = userService.updateUser(identificationNumber, userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Updated", result.getFirstName());
        verify(userMapper, times(1)).updateEntityFromDTO(userRequest, existingUser);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void updateUserShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Long identificationNumber = 123456789L;
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setIdentificationNumber(identificationNumber);

        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(identificationNumber, userRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void deleteByIdentificationNumberShouldDeleteUser() throws HttpClientException {
        // Arrange
        Long identificationNumber = 123456789L;
        User user = new User();
        user.setId(1L);
        user.setIdentificationNumber(identificationNumber);

        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.of(user));

        // Act
        userService.deleteByIdentificationNumber(identificationNumber);

        // Assert
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteByIdentificationNumberShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Long identificationNumber = 123456789L;
        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteByIdentificationNumber(identificationNumber));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    public void isUserOver18ShouldReturnTrueForAdult() {
        // Arrange
        LocalDate adultBirthDate = LocalDate.now().minusYears(20);

        // Act
        boolean result = userService.isUserOver18(adultBirthDate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isUserOver18ShouldReturnFalseForMinor() {
        // Arrange
        LocalDate minorBirthDate = LocalDate.now().minusYears(17);

        // Act
        boolean result = userService.isUserOver18(minorBirthDate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void isUserOver18ShouldReturnTrueForExactly18() {
        // Arrange
        LocalDate exactly18BirthDate = LocalDate.now().minusYears(18);

        // Act
        boolean result = userService.isUserOver18(exactly18BirthDate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void getUserEntityByIdentificationNumberShouldReturnUser() throws HttpClientException {
        // Arrange
        Long identificationNumber = 123456789L;
        User user = new User();
        user.setId(1L);
        user.setIdentificationNumber(identificationNumber);

        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserEntityByIdentificationNumber(identificationNumber);

        // Assert
        assertNotNull(result);
        assertEquals(identificationNumber, result.getIdentificationNumber());
    }

    @Test
    public void getUserEntityByIdentificationNumberShouldThrowExceptionWhenNotFound() {
        // Arrange
        Long identificationNumber = 123456789L;
        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserEntityByIdentificationNumber(identificationNumber));
    }

    @Test
    public void createUserShouldThrowExceptionWhenUserAlreadyExists() throws HttpClientException {
        // Arrange
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setBirthDate(LocalDate.now().minusYears(20));
        userRequest.setIdentificationNumber(123456789L);

        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findByIdentificationNumber(123456789L)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> userService.createUser(userRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void findAllShouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());
        when(userMapper.toResponseDTOList(Arrays.asList())).thenReturn(Arrays.asList());

        // Act
        List<UserResponseDTO> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void updateUserShouldThrowExceptionWhenUserIsUnder18() {
        // Arrange
        Long identificationNumber = 123456789L;
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setIdentificationNumber(identificationNumber);
        userRequest.setBirthDate(LocalDate.now().minusYears(17)); // Under 18

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setIdentificationNumber(identificationNumber);

        when(userRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(identificationNumber, userRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void findByIdShouldReturnUserWhenExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        // Act
        Optional<UserResponseDTO> result = userService.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenNotExists() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<UserResponseDTO> result = userService.findById(userId);

        // Assert
        assertFalse(result.isPresent());
    }
}