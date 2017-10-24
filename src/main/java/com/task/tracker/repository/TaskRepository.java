package com.task.tracker.repository;

import com.task.tracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface TaskRepository extends JpaRepository<Task, Integer> {
    public Task findById(Integer id);
}