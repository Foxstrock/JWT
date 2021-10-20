package com.example.login.Api;

import com.example.login.Service.UserService;
import com.example.login.model.User;
import com.example.login.repository.RoleRepository;
import com.example.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;



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


        user.setPassword(passwordEncoder.encode(password));

        userService.sendMail(user.getEmail(), password);

        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @PutMapping("/forgotpassword")
    private ResponseEntity<User> forgotPassword(@RequestBody String username){
        User newUser = userRepository.findByUsername(username);
        newUser.setTempPassword(true);
        String password;

        UUID StringGe = UUID.randomUUID();
        password = StringGe.toString().replace("-","");
        password = password.replace(".","");


        newUser.setPassword(passwordEncoder.encode(password));

        userService.sendMail(newUser.getEmail(), password);

        return ResponseEntity.ok().body(userRepository.save(newUser));

    }

    @PutMapping("/changepassword")
    private ResponseEntity<User> changePassword(@RequestBody String username, String newPassword){
        User user = userRepository.findByUsername(username);

        newPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        user.setTempPassword(false);

        return ResponseEntity.ok().body(userRepository.save(user));
    }
}
