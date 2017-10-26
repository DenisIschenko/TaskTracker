package com.task.tracker.service;

import com.task.tracker.form.TaskForm;
import com.task.tracker.model.Task;

import java.util.List;

public interface TaskService {
    public List findAll();
    public Task findById(Integer id);
    public Task create(TaskForm taskForm);
    public void update(Task task, TaskForm postForm);
}