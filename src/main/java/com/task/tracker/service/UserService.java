package com.task.tracker.service;

import com.task.tracker.model.User;

import java.util.List;

public interface UserService {
    void save(User user);
    User findByLogin(String login);
    List<User> findAll();
}