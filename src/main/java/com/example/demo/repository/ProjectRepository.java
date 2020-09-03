package com.example.demo.repository;

import com.example.demo.model.Project;
import com.example.demo.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {

//    @Query(value = "select * from project \n" +
//            "INNER JOIN user_project on user_project.project_id = project.id\n" +
//            "where user_project.user_id = ?",
//            nativeQuery = true)

    @Query(value = "select p from project p left join fetch p.userList c where c.id = :id")
    List<Project> findAllByUser(@Param("id") int id);

}
