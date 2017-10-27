package com.task.tracker.service;

import com.task.tracker.form.TaskForm;
import com.task.tracker.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {
    List findAll();
    Task findById(Integer id);
    Task create(TaskForm taskForm);
    void update(Task task, TaskForm taskForm);
    List<Task> findByStatuses(List<Task.Status> statuses);
    List<Task> findAllByCreationDateBetween(Date start, Date end);
    List<Task> AllByCreationDateAndStatuses(List<Task.Status> statuses, Date start, Date end);
}