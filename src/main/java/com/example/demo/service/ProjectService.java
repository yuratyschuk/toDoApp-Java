package com.example.demo.service;

import com.example.demo.exception.DataNotFoundException;
import com.example.demo.model.Project;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
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

    public Project getById(int id) {
        Optional<Project> projectOptional = projectRepository.findById(id);

        if (projectOptional.isPresent()) {
            return projectOptional.get();
        } else {
            throw new DataNotFoundException("Project with id: " + id + " not found");
        }
    }

    public List<Project> getAll() {
        return (List<Project>) projectRepository.findAll();
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

    public List<Project> getAllByUserId(int id) {
        return projectRepository.findAllByUser(id);
    }

    public void share(String username, int projectId) {
        User user = userService.getByUsername(username);
        Project project = getById(projectId);
        project.setUserList(Collections.singletonList(user));
        save(project);
    }

}
