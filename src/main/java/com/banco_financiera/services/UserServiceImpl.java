package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.dto.UserRequestDTO;
import com.banco_financiera.dto.UserResponseDTO;
import com.banco_financiera.exception.business.DuplicateResourceException;
import com.banco_financiera.exception.business.UserNotFoundException;
import com.banco_financiera.mapper.UserMapper;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        log.debug("Finding all users");
        List<User> users = (List<User>) userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findByIdentificationNumber(Long identificationNumber) {
        log.debug("Finding user by identification number: {}", identificationNumber);
        return userRepository.findByIdentificationNumber(identificationNumber)
                .map(userMapper::toResponseDTO);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws HttpClientException {
        log.debug("Creating user with identification number: {}", userRequestDTO.getIdentificationNumber());

        try {
            // Validate age
            if (!isUserOver18(userRequestDTO.getBirthDate())) {
                throw new IllegalArgumentException("User must be at least 18 years old");
            }

            // Check for duplicate identification number
            if (userRepository.findByIdentificationNumber(userRequestDTO.getIdentificationNumber()).isPresent()) {
                throw new DuplicateResourceException("User", "identification_number", userRequestDTO.getIdentificationNumber());
            }

            User user = userMapper.toEntity(userRequestDTO);
            User savedUser = userRepository.save(user);

            log.info("User created successfully with ID: {}", savedUser.getId());
            return userMapper.toResponseDTO(savedUser);

        } catch (DataIntegrityViolationException e) {
            log.warn("Data integrity violation when creating user: {}", e.getMessage());
            throw new DuplicateResourceException("User", "email or identification_number", userRequestDTO.getEmail());
        }
    }

    @Override
    public UserResponseDTO updateUser(Long identificationNumber, UserRequestDTO userRequestDTO) throws HttpClientException {
        log.debug("Updating user with identification number: {}", identificationNumber);

        User existingUser = getUserEntityByIdentificationNumber(identificationNumber);

        // Validate age if birth date is being updated
        if (!isUserOver18(userRequestDTO.getBirthDate())) {
            throw new IllegalArgumentException("User must be at least 18 years old");
        }

        try {
            userMapper.updateEntityFromDTO(userRequestDTO, existingUser);
            User updatedUser = userRepository.save(existingUser);

            log.info("User updated successfully with ID: {}", updatedUser.getId());
            return userMapper.toResponseDTO(updatedUser);

        } catch (DataIntegrityViolationException e) {
            log.warn("Data integrity violation when updating user: {}", e.getMessage());
            throw new DuplicateResourceException("User", "email", userRequestDTO.getEmail());
        }
    }

    @Override
    public void deleteByIdentificationNumber(Long identificationNumber) throws HttpClientException {
        log.debug("Deleting user with identification number: {}", identificationNumber);

        User user = getUserEntityByIdentificationNumber(identificationNumber);
        userRepository.delete(user);

        log.info("User deleted successfully with identification number: {}", identificationNumber);
    }

    @Override
    public boolean isUserOver18(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }

        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        return period.getYears() >= 18;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByIdentificationNumber(Long identificationNumber) throws HttpClientException {
        log.debug("Getting user entity by identification number: {}", identificationNumber);

        return userRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new UserNotFoundException(identificationNumber, true));
    }
}