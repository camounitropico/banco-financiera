package com.banco_financiera.controllers;

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

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{identification_number}")
    public ResponseEntity<User> getUserById(@PathVariable Long identificationNumber) {
        Optional<User> user = userService.findById(identificationNumber);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public User createUser(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @PutMapping("/{identification_number}")
    public ResponseEntity<User> updateUser(@PathVariable Long identificationNumber, @RequestBody UserRequest userDetails) {
        Optional<User> user = userService.findById(identificationNumber);
        if (user.isPresent()) {
            return ResponseEntity.ok(userService.save(userDetails));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{identification_number}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long identificationNumber) {
        if (userService.findById(identificationNumber).isPresent()) {
            userService.deleteById(identificationNumber);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
