package com.example.lms.controller;

import com.example.lms.config.JwtService;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.AdminService;
import com.example.lms.service.UserServiceImp;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private JwtService jwtService;

    // Example: Create a user by verifying the role through ID

    @PostMapping("/createUser")
    @RolesAllowed({"ADMIN"})

    public ResponseEntity<?> createUser(@RequestBody User user) {

        User newUser = userServiceImp.createUser(user);
        String token = jwtService.generateToken(newUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("/viewAdminProfile")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the user ID from the token
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // Assuming the token contains an "id" claim

        // Fetch the user by ID (if needed)
        User admin = userServiceImp.getUserByEmail(email);

        // Return the user with HTTP status 200 OK
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/viewProfile/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> viewUserById(@PathVariable int id) {

        User user = userServiceImp.getUserById(id);

        return ResponseEntity.ok(user);

    }

    @PutMapping("/updateAdminProfile")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateById(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody User user){
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // Assuming the token contains an "id" claim

        // Fetch the user by ID (if needed)
        User admin = userServiceImp.getUserByEmail(email);
        userServiceImp.updateById(admin.getId(), user);
        user.setId(admin.getId());
        return ResponseEntity.ok(user);

    }

    @PutMapping("/updateProfile/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateUserById(@PathVariable int id,
                                        @RequestBody User user){

        userServiceImp.updateById(id, user);
        user.setId(id);
        return ResponseEntity.ok(user);

    }

    @DeleteMapping("/deleteUser/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteById(@PathVariable int id){

        userServiceImp.deleteById(id);
        return ResponseEntity.ok("deleted successfully");

    }



}
