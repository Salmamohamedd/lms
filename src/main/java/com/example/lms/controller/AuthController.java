package com.example.lms.controller;

import com.example.lms.model.AuthModel;
import com.example.lms.model.User;
import com.example.lms.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService service;

//    @Autowired
//    private UserService
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthModel authModel){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authModel.getEmail(),
                        authModel.getPassword()));
        return new ResponseEntity<>(HttpStatus.OK);


    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

}
