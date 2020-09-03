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



    public Project share(String credentials, int projectId) throws Exception {
        User user;
        if(credentials.contains("@")) {
            user = userService.getByEmail(credentials);
        } else {
            user = userService.getByUsername(credentials);
        }


        Project project = getById(projectId);
        if(project.getUserList().contains(user)) {
            throw new Exception("Project already shared: " + project.getName());
        }
        project.setUserList(Collections.singletonList(user));

        return save(project);
    }

}
