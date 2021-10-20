package com.example.login.Api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.login.Service.UserService;
import com.example.login.model.Role;
import com.example.login.model.User;
import com.example.login.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

    @PostMapping("/save")
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

    @GetMapping("/token/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){

            try{
                String refresh_token = authorizationHeader.substring("Bearer".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles" , user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()))
                        .sign(algorithm);
                response.setHeader("Access Token" , access_token);
                response.setHeader("Refresh Token" , refresh_token);

            }catch (Exception exception){
                exception.printStackTrace();
                response.setHeader("Error" , exception.getMessage());
            }

        }else {
            throw new RuntimeException("Refresh the token is missing");
        }
    }

}
