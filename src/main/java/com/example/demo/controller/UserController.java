package com.example.demo.controller;

import com.example.demo.exception.DataNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class    UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping(value = "/getAll")
    public ResponseEntity<Iterable<User>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }


    @PostMapping(value = "/register")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldError().toString());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @GetMapping(value = "/subscribe")
    public ResponseEntity<?> subscribeUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found. Username: " + authentication.getName()));

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") int userId) {

        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getById(userId)
        .orElseThrow(() -> new DataNotFoundException("User not found. Id: " + userId)));
    }

    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
        user.setId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") int userId) {
        userService.deleteById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
