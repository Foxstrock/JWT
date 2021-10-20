package com.example.login.Service;

import com.example.login.model.Role;
import com.example.login.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username ,String roleName);
    User getUser(String username);
    List<User> getUsers();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    void deleteAll();
}
