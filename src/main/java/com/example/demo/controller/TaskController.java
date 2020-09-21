package com.example.demo.controller;

import com.example.demo.exception.AlreadySharedException;
import com.example.demo.exception.DataNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.service.ProjectService;
import com.example.demo.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    private final ProjectService projectService;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;

    }

    @GetMapping(value = "/list/{projectId}")
    public ResponseEntity<Iterable<Task>> getTaskListByProjectId(@PathVariable("projectId") int projectId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(taskService.getTaskByProjectId(projectId));
    }

    @PostMapping(value = "/save/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> saveTask(@Valid @RequestBody Task task, BindingResult bindingResult,
                                         @PathVariable("projectId") int projectId) {


        Project project = projectService.getById(projectId)
                .orElseThrow(() -> new DataNotFoundException("Project not found. Id: " + projectId));
        task.setProject(project);

        if(bindingResult.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(bindingResult.getFieldError()).toString());
        }


        return ResponseEntity.status(HttpStatus.OK).body(taskService.save(task));
    }

    @DeleteMapping(value = "/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") int taskId) {
        taskService.deleteById(taskId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/update/{taskId}/projects/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @PathVariable("taskId") int taskId,
                                           @PathVariable("projectId") int projectId) {
        Project project = projectService.getById(projectId)
                .orElseThrow(() -> new DataNotFoundException("Project not found. Id: " + projectId));
        task.setId(taskId);
        task.setProject(project);

        return ResponseEntity.status(HttpStatus.OK).body(taskService.update(task));
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable("taskId") int taskId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(taskService.getById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Task not found. Id: " + taskId)));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<Iterable<Task>> getAllTasks() {
        return ResponseEntity.status(HttpStatus.FOUND).body(taskService.getAll());
    }

    @PutMapping(value = "/active/{taskId}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable("taskId") int taskId) {

        Task task = taskService.getById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Task not found. Id: " + taskId));

        task = taskService.changeStatus(task);
        return ResponseEntity.status(HttpStatus.OK).body(taskService.update(task));
    }

    @GetMapping(value = "/active/{taskId}/{projectId}")
    public ResponseEntity<Iterable<Task>> getTaskByStatus(@RequestParam("isActive") String isActive,
                                                          @PathVariable("projectId") int projectId) {

        return ResponseEntity.status(HttpStatus.FOUND).body(taskService.getTaskByProjectIdAndActive(projectId,
                Boolean.parseBoolean(isActive)));
    }

    @PutMapping(value = "/priority/{taskId}")
    public ResponseEntity<?> updateTaskPriority(@RequestParam("priority") int priority,
                                                @PathVariable("taskId") int taskId) {
        taskService.updatePriority(priority, taskId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
