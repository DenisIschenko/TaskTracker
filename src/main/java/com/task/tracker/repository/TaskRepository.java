package com.task.tracker.repository;

import com.task.tracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository()
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findById(Integer id);
    List<Task> findAllByStatusIsIn(List<Task.Status> statuses);
    List<Task> findAllByCreationDateBetween(Date start, Date end);

    List<Task> findAllByStatusIsInAndCreationDateBetween(List<Task.Status> statuses, Date start, Date end);
}