package com.task.tracker.service;

import com.task.tracker.form.TaskForm;
import com.task.tracker.model.Task;
import com.task.tracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

//    @Override
//    public void save(Task task) {
//        task.setCreationDate(new Date());
//        taskRepository.save(task);
//    }

    @Override
    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Integer id){
        return taskRepository.findById(id);
    }

    public Task create(TaskForm taskForm) {
        Task task = new Task();
        task.setTitle(taskForm.getTitle());
        task.setDescription(taskForm.getDescription());
        task.setStatus(taskForm.getStatus());
        task.setCreationDate(new Date());
        task.setOwner(taskForm.getOwner());
        taskRepository.save(task);
        return task;
    }

    public void update(Task task, TaskForm taskForm) {
        task.setTitle(taskForm.getTitle());
        task.setDescription(taskForm.getDescription());
        task.setStatus(taskForm.getStatus());
        task.setOwner(taskForm.getOwner());

        taskRepository.save(task);
    }

}