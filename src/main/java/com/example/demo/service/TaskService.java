package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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

        if(task.getPriority() == -1) {
            task.setPriority(0);
        }

        return taskRepository.save(task);
    }

    public Optional<Task> getById(int id) {
        return taskRepository.findById(id);
    }

    public Iterable<Task> getAll() {
        return taskRepository.findAll();
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

    public Iterable<Task> getTaskByProjectId(int id) {
       return taskRepository.getAllByProjectId(id);
    }

    public Iterable<Task> getTaskByProjectIdAndActive(int projectId, boolean active) {
        return taskRepository.getAllByProjectIdAndActive(projectId, active);
    }

    public Task changeStatus(Task task) {
        task.setActive(!task.isActive());

        return save(task);
    }

    public Task updatePriority(int priority, int taskId) {
        return taskRepository.updatePriority(priority, taskId);
    }
}
