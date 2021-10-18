package com.example.login.Service;

import com.example.login.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    Optional<User> getUser(String username);
    List<User> getUsers();
}