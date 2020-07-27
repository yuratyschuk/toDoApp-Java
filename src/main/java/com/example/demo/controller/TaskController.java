package com.example.demo.controller;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.service.ProjectService;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    TaskService taskService;

    ProjectService projectService;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;

    }

    @InitBinder
    public void setup(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }

    @GetMapping(value = "/taskList/{projectId}")
    public ResponseEntity<List<Task>> showTaskListPage(@PathVariable("projectId") int projectId) {
        return ResponseEntity.ok().body(taskService.getTaskByProjectId(projectId));
    }

    @PostMapping(value = "/taskSave/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Task postTaskSave(@RequestBody Task task, @PathVariable("projectId") int projectId) {
        System.out.println(task);
        Project project = projectService.getById(projectId);
        task.setProject(project);
        task.setActive(true);
        return taskService.save(task);
    }

    @DeleteMapping(value = "/taskDelete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") int taskId) {
        taskService.deleteById(taskId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/taskUpdate/{taskId}/projects/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @PathVariable("taskId") int taskId,
                                           @PathVariable("projectId") int projectId) {
        Project project = projectService.getById(projectId);
        task.setId(taskId);
        task.setProject(project);

        return ResponseEntity.ok().body(taskService.update(task));
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable("taskId") int taskId) {
        return ResponseEntity.ok().body(taskService.getById(taskId));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getAll());
    }

    @PutMapping(value = "/taskActive/{taskId}")
    public ResponseEntity<Task> changeTaskStatus(@PathVariable("taskId") int taskId) {

        Task task = taskService.getById(taskId);

        task = taskService.changeStatus(task);
        return ResponseEntity.ok().body(taskService.update(task));
    }

    @GetMapping(value = "/taskActive/{taskId}/{projectId}")
    public ResponseEntity<List<Task>> getTaskByStatus(@RequestParam("isActive") String isActive,
                                                      @PathVariable("projectId") int projectId) {

        return ResponseEntity.ok().body(taskService.getTaskByProjectIdAndActive(projectId,
                Boolean.parseBoolean(isActive)));
    }

}
