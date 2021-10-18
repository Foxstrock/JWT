package com.example.login.repository;

import com.example.login.model.Role;
import com.example.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String name);
}
