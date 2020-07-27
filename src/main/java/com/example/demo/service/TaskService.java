package com.example.demo.service;

import com.example.demo.exception.DataNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {


    TaskRepository taskRepository;

    UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task save(Task task) {
        task.setCreateDate(new Date());
        return taskRepository.save(task);
    }

    public Task getById(int id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            return taskOptional.get();
        } else {
            throw new DataNotFoundException("Task with id: " + id + " not found");
        }
    }

    public List<Task> getAll() {
        return (List<Task>) taskRepository.findAll();
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTaskByProjectId(int id) {
       return taskRepository.getAllByProjectId(id);
    }

    public List<Task> getTaskByProjectIdAndActive(int projectId, boolean active) {
        return taskRepository.getAllByProjectIdAndActive(projectId, active);
    }

    public Task changeStatus(Task task) {
        task.setActive(!task.isActive());

        return task;
    }


}
