package com.task.tracker.service;

import com.task.tracker.form.TaskForm;
import com.task.tracker.model.Task;
import com.task.tracker.model.User;
import com.task.tracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

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
        User user = userService.findByLogin(taskForm.getAssigneeLogin());
        task.setAssignee(user);
        taskRepository.save(task);
        return task;
    }

    public void update(Task task, TaskForm taskForm) {
        task.setTitle(taskForm.getTitle());
        task.setDescription(taskForm.getDescription());
        task.setStatus(taskForm.getStatus());
        User user = userService.findByLogin(taskForm.getAssigneeLogin());
        task.setAssignee(user);
        taskRepository.save(task);
    }

    @Override
    public List<Task> findByStatuses(List<Task.Status> statuses) {
        return taskRepository.findAllByStatusIsIn(statuses);
    }

    @Override
    public List<Task> findAllByCreationDateBetween(Date start, Date end) {
        return taskRepository.findAllByCreationDateBetween(start, end);
    }

    @Override
    public List<Task> AllByCreationDateAndStatuses(List<Task.Status> statuses, Date start, Date end) {
        return taskRepository.findAllByStatusIsInAndCreationDateBetween(statuses, start, end);
    }
}