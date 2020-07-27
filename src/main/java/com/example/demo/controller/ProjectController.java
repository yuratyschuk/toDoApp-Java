package com.example.demo.controller;

import com.example.demo.details.UserDetailsImpl;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/project")
public class ProjectController {

    ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/create")
    public Project postCreateProject(@RequestBody Project project) {

        System.out.println("test: " + project);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        project.setUserList(Collections.singletonList(user));

        return projectService.save(project);
    }

    @DeleteMapping(value = "/delete/{projectId}")
    public ResponseEntity<?> postDeleteProject(@PathVariable("projectId") int projectId) {
        projectService.deleteById(projectId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{projectId}")
    public ResponseEntity<Project> postEditProject(@ModelAttribute Project project) {
        return ResponseEntity.ok().body(projectService.update(project));
    }

    @GetMapping(value = "/projectList")
    public List<Project> getAllProject() {


        return projectService.getAllByUserId(1);
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable("projectId") int projectId) {

        return ResponseEntity.ok().body(projectService.getById(projectId));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok().body(projectService.getAll());
    }
}
