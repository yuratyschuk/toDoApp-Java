package com.example.demo.service;

import com.example.demo.email.sender.Mail;
import com.example.demo.email.sender.MailService;
import com.example.demo.exception.DataNotFoundException;
import com.example.demo.kafka.KafkaService;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserService {

    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    TaskRepository taskService;

    MailService mailService;

    KafkaService kafkaService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       TaskRepository taskService, MailService mailService, KafkaService kafkaService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.taskService = taskService;
        this.mailService = mailService;
        this.kafkaService = kafkaService;
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

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void getByUsernameAndPassword(String username, String password) {
        userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new DataNotFoundException("User with username " + username + " not found"));
    }

    public void checkIfTaskIsOutOfDate() {
        List<Task> taskList = (List<Task>) taskService.findAll();
        Date date = new Date();
        for (Task task : taskList) {
            if (task.getFinishDate() != null && date.compareTo(task.getFinishDate()) > 0) {
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
