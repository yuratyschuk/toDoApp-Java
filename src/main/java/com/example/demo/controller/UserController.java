package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
@SessionAttributes(types = User.class)
public class UserController {

    UserService userService;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @InitBinder
    public void setup(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }

    @GetMapping(value="/getAll")
    public ResponseEntity<List<User>> getAll(ModelMap modelMap) {
        return ResponseEntity.ok().body(userService.getAll());
    }


    @PostMapping(value ="/register")
    public ResponseEntity<User> saveUser(@ModelAttribute("user") User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return ResponseEntity.ok().body(userService.save(user));
    }

    @GetMapping(value = "/getById/{userId}")
    public ResponseEntity<User> getById(@PathVariable("userId") int userId) {

        return ResponseEntity.ok().body(userService.getById(userId));
    }

    @PutMapping(value = "/update/{userId}")
    public User update(@PathVariable("userId") int userId, @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/deleteById/{userId}")
    public void deleteById(@PathVariable("userId") int userId) {
        userService.deleteById(userId);
    }


    @PostMapping(value = "/login")
    public void postLogin(@ModelAttribute("user") User user) {

        User loggedUser = userService.getByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
