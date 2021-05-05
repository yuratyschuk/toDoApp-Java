package com.example.demo.service;

import com.example.demo.email.sender.MailService;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private MailService mailService;

    @Autowired
    @InjectMocks
    private UserService userService;

    private User user;

    @Before
    public void setup() {
        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email");


        Project project = new Project();
        project.setId(1);
        project.setName("Project name");
        project.setUserList(Collections.singletonList(user));

        Task task = new Task();
        task.setTitle("test title 1");
        task.setDescription("test description 1");
        task.setActive(true);
        task.setPriority(2);
        task.setProject(project);
        task.setCreateDate(LocalDateTime.now());
    }

    @Test
    public void testSaveMethod() {
        given(userRepository.save(any(User.class))).willReturn(user);
        given(bCryptPasswordEncoder.encode(anyString())).willReturn("encoded password");

        User savedUser = userService.save(user);

        assertEquals("encoded password", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

}
