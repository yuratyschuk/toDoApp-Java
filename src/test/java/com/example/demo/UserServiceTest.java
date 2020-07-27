package com.example.demo;


import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void test() {
        userService.getByUsernameAndPassword("test", "test");
    }
}
