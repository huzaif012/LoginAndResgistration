package com.LoginAndRegistration.loginAndRegistration.controller;

import com.LoginAndRegistration.loginAndRegistration.Entity.Role;
import com.LoginAndRegistration.loginAndRegistration.Entity.User;
import com.LoginAndRegistration.loginAndRegistration.dto.LoginDto;
import com.LoginAndRegistration.loginAndRegistration.dto.SignUpDto;
import com.LoginAndRegistration.loginAndRegistration.repository.RoleRepository;
import com.LoginAndRegistration.loginAndRegistration.repository.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class HomeController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(HttpServletRequest request, @RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Retrieve session ID
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        return new ResponseEntity<>("User login successfully! Session ID: " + sessionId, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        //checking for username exists in database
        if(signUpDto.getUsername()==null|| signUpDto.getUsername().isEmpty() || signUpDto.getPassword()== null || signUpDto.getPassword().isEmpty()
        || signUpDto.getEmail()==null || signUpDto.getEmail().isEmpty()){
            return new ResponseEntity<>("Username, Password and email should not be null or empty", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUserName(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
        }
        //checking for email exists in database
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        //creating user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUserName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode((signUpDto.getPassword())));
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User is registered succussfylly!",HttpStatus.OK);
    }
}
