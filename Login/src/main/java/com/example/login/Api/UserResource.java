package com.example.login.Api;

import com.example.login.Service.UserService;
import com.example.login.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private final UserService userService;

    @GetMapping("/alluser")
    private ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    private  ResponseEntity<User> saveUser(@RequestBody User user){
        if(userService.getUser(user.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return  ResponseEntity.ok().body(userService.saveUser(user));
    }
}
