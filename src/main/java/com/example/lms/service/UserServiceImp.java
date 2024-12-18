package com.example.lms.service;

import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setRole(user.getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
//    public void addUser(User user){
//        if(userRepository.findByEmail(user.getEmail()).isPresent()){
//            throw new RuntimeException("Email already exists");
//        }
//        userRepository.save(user);
//    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    public boolean findByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public void updateById(int id, User user){
        User existing = getUserById(id);
        existing.setName(user.getName());
        existing.setPassword(passwordEncoder.encode(user.getPassword()));
        existing.setEmail(user.getEmail());
        userRepository.save(existing);

    }

    public void deleteById(int id) {
        if (getUserById(id) != null) {
            User user = getUserById(id);
            userRepository.deleteById(user.getId());
        }

    }
}
