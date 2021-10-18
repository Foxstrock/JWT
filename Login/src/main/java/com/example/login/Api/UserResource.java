package com.example.login.Api;

import com.example.login.Service.UserService;
import com.example.login.model.Role;
import com.example.login.model.User;
import com.example.login.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/alluser")
    private ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    private  ResponseEntity<User> saveUser(@RequestBody User user){
        if(userService.getUser(user.getUsername()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return  ResponseEntity.ok().body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    private  ResponseEntity<Role> saveRole(@RequestBody Role role){
        if(roleRepository.findByRole(role.getRole()) == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return  ResponseEntity.ok().body(roleRepository.save(role));
    }
}
