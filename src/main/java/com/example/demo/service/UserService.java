package com.example.demo.service;

import com.example.demo.email.sender.Mail;
import com.example.demo.email.sender.MailService;
import com.example.demo.exception.DataNotFoundException;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final TaskRepository taskService;

    private final MailService mailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       TaskRepository taskService, MailService mailService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.mailService = mailService;
    }

    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRepeatedPassword("null");
        return userRepository.save(user);
    }

    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    public Iterable<User> getAll() {
        return  userRepository.findAll();
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void getByUsernameAndPassword(String username, String password) {
        userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new DataNotFoundException("User with username " + username + " not found"));
    }

    public void checkIfTaskIsOutOfDate() {
        List<Task> taskList = (List<Task>) taskService.findAll();
        for (Task task : taskList) {
            if (task.getFinishDate() != null && LocalDateTime.now().isAfter(task.getFinishDate())) {
                configureEmailBeforeSending(task);
            }
        }
    }

    public void configureEmailBeforeSending(Task task) {
        Project project = task.getProject();

        for(User user : project.getUserList()) {
            Mail mail = new Mail();
            mail.setMailFrom("toDoApp@gmail.com");
            mail.setMailTo(user.getEmail());
            mail.setMailSubject("ToDo App");
            mail.setMailContent("You have expired task: " + task.getTitle());
            mailService.sendEmail(mail);
        }
    }
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
