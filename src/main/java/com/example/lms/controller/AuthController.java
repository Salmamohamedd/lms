package com.example.lms.controller;

import com.example.lms.config.ApplicationConfig;
import com.example.lms.config.JwtService;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ApplicationConfig applicationConfig;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
    if(userRepository.findByEmail(user.getEmail()).isPresent()){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());
        user = userRepository.save(newUser);
        String token = jwtService.generateToken(user);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User foundUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(foundUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = jwtService.generateToken(foundUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", foundUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
