package com.task.tracker.service;

import com.task.tracker.model.User;

import java.util.List;

public interface UserService {
    public void save(User user);
    public User findByLogin(String login);
    public List<User> findAll();
}