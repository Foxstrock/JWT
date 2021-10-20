package com.example.login.Api;

import com.example.login.model.User;
import com.example.login.repository.RoleRepository;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/registration")
public class RegisterService {
    @Autowired
    UserRepository userRepository;
    RoleRepository roleRepository;


    @PostMapping("/register")
    private ResponseEntity<User> addNewUser(@RequestBody User user){
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        String password;

        if(!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).find()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        UUID StringGe = UUID.randomUUID();
        password = StringGe.toString().replace("-","");
        password = password.replace(".","");
        user.setPassword(password);

        return ResponseEntity.ok().body(userRepository.save(user));
    }
}
