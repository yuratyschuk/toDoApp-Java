package com.example.demo.controller;

import com.example.demo.details.UserDetailsImpl;
import com.example.demo.exception.DataNotFoundException;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @PostMapping(value = "/save")
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        project.setUserList(Collections.singletonList(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.save(project));
    }


    @DeleteMapping(value = "/delete/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable("projectId") int projectId) {
        projectService.deleteById(projectId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{projectId}")
    public ResponseEntity<Project> updateProject(@ModelAttribute Project project) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.update(project));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Iterable<Project>> getAllProjectsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByUsername(authentication.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found. Username: " + authentication.getName()));


        return ResponseEntity.status(HttpStatus.FOUND).body(projectService.getAllByUserId(user.getId()));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable("projectId") int projectId) {

        return ResponseEntity.status(HttpStatus.FOUND).body(projectService.getById(projectId)
        .orElseThrow(() -> new DataNotFoundException("Project not found. Id: " + projectId)));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<Iterable<Project>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.FOUND).body(projectService.getAll());
    }

    @PostMapping(value = "/share/{projectId}")
    public ResponseEntity<Project> shareProject(@PathVariable("projectId") int projectId,
                                                @RequestParam String credentials) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.share(credentials, projectId));
    }
}
