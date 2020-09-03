package com.example.demo.service;

import com.example.demo.exception.DataNotFoundException;
import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    ProjectRepository projectRepository;

    UserService userService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }


    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> getById(int id) {
        return projectRepository.findById(id);
    }

    public Iterable<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project update(Project project) {
        return projectRepository.save(project);
    }

    public void delete(Project project) {
        projectRepository.delete(project);
    }

    public void deleteById(int id) {
        projectRepository.deleteById(id);
    }

    public Iterable<Project> getAllByUserId(int id) {
        return projectRepository.findAllByUser(id);
    }


    public Project share(String credentials, int projectId) throws Exception {
        User user;
        if (credentials.contains("@")) {
            user = userService.getByEmail(credentials)
                    .orElseThrow(() -> new DataNotFoundException("User not found. Email: " + credentials));
        } else {
            user = userService.getByUsername(credentials)
                    .orElseThrow(() -> new DataNotFoundException("User not found. Username: " + credentials));
        }


        Optional<Project> projectOptional = getById(projectId);
        Project project = projectOptional
                .orElseThrow(() -> new DataNotFoundException("Project not found. Id: " + projectId));

        if (project.getUserList().contains(user)) {
            throw new Exception("Project already shared: " + project.getName());
        }

        project.setUserList(Collections.singletonList(user));

        return save(project);
    }

}
