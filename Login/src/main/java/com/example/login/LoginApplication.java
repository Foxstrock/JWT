package com.example.login;

import com.example.login.Service.UserService;
import com.example.login.model.Role;
import com.example.login.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class LoginApplication {


    public static void main(String[] args) {

        SpringApplication.run(LoginApplication.class, args);

    }

    @Bean
   PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }



    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.deleteAll();

            userService.saveRole(new Role(null , "ROLE_USER"));
            userService.saveRole(new Role(null , "ROLE_MANAGER"));
            userService.saveRole(new Role(null , "ROLE_ADMIN"));
            userService.saveRole(new Role(null , "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null , "Foxstrock@gmail.com","user","Marcos","Sottile","Ciao","14/02/2002",false,new ArrayList<>()));

            userService.addRoleToUser("user" , "ROLE_ADMIN");
            userService.addRoleToUser("user" , "ROLE_USER");
            userService.addRoleToUser("user" , "ROLE_MANAGER");
            userService.addRoleToUser("user" , "ROLE_SUPER_ADMIN");





        };
    }



}
