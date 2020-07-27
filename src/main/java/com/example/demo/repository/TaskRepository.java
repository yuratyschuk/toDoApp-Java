package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Query(value = "select * from task where task.project_id = ?", nativeQuery = true)
    List<Task> getAllByProjectId(int id);

    @Query(value = "select * from task where task.project_id = ? AND task.active = ?", nativeQuery = true)
    List<Task> getAllByProjectIdAndActive(int projectId, boolean active);

}
