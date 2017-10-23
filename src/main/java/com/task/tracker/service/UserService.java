package com.task.tracker.service;

import com.task.tracker.model.User;

public interface UserService {
    public void save(User user);
    public User findByLogin(String login);
}