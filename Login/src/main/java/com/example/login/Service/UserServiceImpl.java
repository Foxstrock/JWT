package com.example.login.Service;

import com.example.login.model.Role;
import com.example.login.model.User;
import com.example.login.repository.RoleRepository;
import com.example.login.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService,UserDetailsService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Override
    public User saveUser(User user) {
        log.info( "Saving new user",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info( "Saving new role {}" , role.getRole());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}",roleName , username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByRole(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}" , username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all user");
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        }else{
            log.info("User found in Database : {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        log.info("All element are Elimineted");
    }

    @Override
    public void sendMail(String email,String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = new User();
        message.setTo(email);
        message.setSubject("First Access");
        message.setText("Your password is :" + password);

        javaMailSender.send(message);

    }


}

@Data
class RoleToUserFrom{
    String username;
    String roleName;
}
