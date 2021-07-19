package com.example.demo.controller;

import com.example.demo.exception.DataNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<Iterable<User>> getAll() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @PostMapping(value = "/register")
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("User validation error. Fields: {}", Objects.requireNonNull(bindingResult.getFieldError()).getField());
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).getField());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") int userId) {
        User user = userService.getById(userId).orElseThrow(() -> {
            logger.error("User not found. Id: {}", userId);
            return new DataNotFoundException("User not found. Id: " + userId);
        });

        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
        user.setId(userId);

        return ResponseEntity.ok().body(userService.update(user));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") int userId) {
        userService.deleteById(userId);

        return ResponseEntity.ok().build();
    }
}
