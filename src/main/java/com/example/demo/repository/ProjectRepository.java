package com.example.demo.repository;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {

    @Query(value = "select * from project \n" +
            "join user_project on user_project.project_id = project.id\n" +
            "where user_project.user_id = ?",
            nativeQuery = true)
    List<Project> findAllByUser(int id);
}
