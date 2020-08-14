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

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/create")
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

    @GetMapping(value = "/projectList")
    public ResponseEntity<List<Project>> getAllProjectsByUserId() {
//        return projectService.getAllByUserId(1);
        return ResponseEntity.status(HttpStatus.FOUND).body(projectService.getAllByUserId(1));
    }

    @GetMapping(value = "/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable("projectId") int projectId) {

        return ResponseEntity.status(HttpStatus.FOUND).body(projectService.getById(projectId));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.FOUND).body(projectService.getAll());
    }

    @PostMapping(value = "/share/{projectId}")
    public ResponseEntity<Project> shareProject(@PathVariable("projectId") int projectId,
                                                @RequestParam String credentials) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.share(credentials, projectId));
    }
}
