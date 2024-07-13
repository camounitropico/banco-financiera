package com.banco_financiera.services;

import com.banco_financiera.models.User;
import com.banco_financiera.repositories.UserRepository;
import com.banco_financiera.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User save(UserRequest userRequest) {
        User user = new User();
        user.setIdentificationType(userRequest.getIdentificationType());
        user.setIdentificationNumber(userRequest.getIdentificationNumber());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setBirthDate(userRequest.getBirthDate());

        return userRepository.save(user);
    }

    public void deleteById(Long identificationNumber) {
        userRepository.deleteById(identificationNumber);
    }

    public void update(Long identuficationNumber, UserRequest userDetails) {
        Optional<User> user = userRepository.findById(identuficationNumber);
        if (user.isPresent()) {
            this.save(userDetails);
        }
    }
}
