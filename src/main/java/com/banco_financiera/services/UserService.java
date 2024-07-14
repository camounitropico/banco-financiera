package com.banco_financiera.services;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.User;
import com.banco_financiera.repositories.UserRepository;
import com.banco_financiera.requests.UserRequest;
import com.banco_financiera.utils.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long identificationNumber) {
        return userRepository.findById(identificationNumber);
    }

    public Optional<User> findByIdentificationNumber(Long identificationNumber) {
        return userRepository.findByIdentificationNumber(identificationNumber);
    }

    public User save(UserRequest userRequest) throws HttpClientException {
        try {
            if (isUserOver18(userRequest.getBirthDate())) {
                User user = new User();
                user.setIdentificationType(userRequest.getIdentificationType());
                user.setIdentificationNumber(userRequest.getIdentificationNumber());
                validateAndSet(
                        StringValidator::isValidName,
                        userRequest::getFirstName,
                        user::setFirstName,
                        "Invalid first name"
                );
                validateAndSet(
                        StringValidator::isValidName,
                        userRequest::getLastName,
                        user::setLastName,
                        "Invalid last name"
                );
                validateAndSet(
                        StringValidator::isValidEmail,
                        userRequest::getEmail,
                        user::setEmail,
                        "Invalid email"
                );

                user.setEmail(userRequest.getEmail());
                user.setBirthDate(userRequest.getBirthDate());

                return userRepository.save(user);
            } else throw new IllegalArgumentException("invalid user, the user is not of legal age");

        } catch (Exception e) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, e);
        }
    }

    public void deleteById(Long identificationNumber) {
        userRepository.deleteById(identificationNumber);
    }

    public User upDateUser(UserRequest userRequest, User user) throws HttpClientException {
        try {
            user.setIdentificationType(userRequest.getIdentificationType());
            user.setIdentificationNumber(userRequest.getIdentificationNumber());
            validateAndSet(
                    StringValidator::isValidName,
                    userRequest::getFirstName,
                    user::setFirstName,
                    "Invalid first name"
            );
            validateAndSet(
                    StringValidator::isValidName,
                    userRequest::getLastName,
                    user::setLastName,
                    "Invalid last name"
            );
            validateAndSet(
                    StringValidator::isValidEmail,
                    userRequest::getEmail,
                    user::setEmail,
                    "Invalid email"
            );

            user.setEmail(userRequest.getEmail());
            user.setBirthDate(userRequest.getBirthDate());

            return userRepository.save(user);
        } catch (Exception e) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, e);
        }
    }

    private void validateAndSet(
            Predicate<String> validator,
            Supplier<String> valueSupplier,
            Consumer<String> valueSetter,
            String errorMessage
    ) {
        String value = valueSupplier.get();
        if (validator.test(value)) {
            valueSetter.accept(value);
        } else {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public boolean isUserOver18(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        return period.getYears() >= 18;
    }
}
