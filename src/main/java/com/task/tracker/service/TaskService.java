package com.task.tracker.service;

import com.task.tracker.model.Task;

import java.util.List;

public interface TaskService {
    public void save(Task task);
    public List findAll();
    public Task findById(Integer id);
}