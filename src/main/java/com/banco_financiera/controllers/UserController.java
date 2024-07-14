package com.banco_financiera.controllers;

import com.banco_financiera.core.exceptions.HttpClientException;
import com.banco_financiera.models.User;
import com.banco_financiera.requests.UserRequest;
import com.banco_financiera.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public String healthCheck() {
        return "i am up";
    }

    @GetMapping("/get-all")
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{identification_number}")
    public ResponseEntity<User> getUserById(@PathVariable Long identification_number) {
        Optional<User> user = userService.findByIdentificationNumber(identification_number);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public User createUser(@RequestBody UserRequest userRequest) throws HttpClientException {
        return userService.save(userRequest);
    }

    @PutMapping("/{identification_number}")
    public ResponseEntity<User> updateUser(@PathVariable Long identification_number, @RequestBody UserRequest userDetails) throws HttpClientException {
        Optional<User> user = userService.findByIdentificationNumber(identification_number);
        if (user.isPresent()) {
            userDetails.setIdentificationNumber(identification_number);
            return ResponseEntity.ok(userService.save(userDetails));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{identification_number}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long identification_number) {
        if (userService.findByIdentificationNumber(identification_number).isPresent()) {
            userService.deleteByIdentificationNumber(identification_number);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
