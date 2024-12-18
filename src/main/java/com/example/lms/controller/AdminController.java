package com.example.lms.controller;

import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.AdminService;
import com.example.lms.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServiceImp userServiceImp;


    // Example: Create a user by verifying the role through ID

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/createUser")
    public ResponseEntity<String> createUser(@PathVariable("id") int id, @RequestBody User user) {
        if (!isAdmin(id)) {
            return ResponseEntity.status(403).body("Access denied: User is not an admin");
        }

        System.out.println(user.getEmail());
        System.out.println(11111);
        System.out.println(user.getName());
        System.out.println(user.getRole());
        System.out.println(user.getPassword());
        // If the user is an admin, proceed with creating the user
        userServiceImp.addUser(user);
        return ResponseEntity.ok("User created successfully by Admin");
    }

    // Example: Manage courses (Admin-only functionality using ID)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/manageCourses")
    public ResponseEntity<String> manageCourses(@PathVariable("id") int id) {
//        if (!isAdmin(id)) {
//            return ResponseEntity.status(403).body("Access denied: User is not an admin");
//        }
//        User user = userOptional.get();
//
//        // Check if the user's role is ADMIN
//        if (!user.getRole().equals("ADMIN")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: User is not an admin");
//        }
//
//        // If the user is an admin, proceed with managing courses
        return ResponseEntity.ok("Courses managed by Admin");
    }

    // Example: Manage courses (Admin-only functionality using ID)
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/viewAdminProfile")
    public ResponseEntity<?> getById(@PathVariable int id) {
        if (!isAdmin(id)) {
            return ResponseEntity.status(403).body("Access denied: User is not an admin");
        }
        User admin = userServiceImp.getUserById(id);

        // Return the user if the role is ADMIN with HTTP status 200 OK
        return ResponseEntity.ok(admin);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/viewProfile/{id2}")
    public ResponseEntity<?> viewUserById(@PathVariable int id, @PathVariable int id2) {
        if (!isAdmin(id)) {
            return ResponseEntity.status(403).body("Access denied: User is not an admin");
        }
        User user = userServiceImp.getUserById(id2);

        return ResponseEntity.ok(user);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/updateAdminProfile")
    public ResponseEntity<?> updateById(@PathVariable int id,
                                        @RequestBody User user){
        if (!isAdmin(id)) {
            return ResponseEntity.status(403).body("Access denied: User is not an admin");
        }
        userServiceImp.updateById(id, user);
        return ResponseEntity.ok(user);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/updateProfile/{id2}")
    public ResponseEntity<?> updateUserById(@PathVariable int id,
                                        @PathVariable int id2,
                                        @RequestBody User user){
        if (!isAdmin(id)) {
            return ResponseEntity.status(403).body("Access denied: User is not an admin");
        }
        userServiceImp.updateById(id2, user);
        return ResponseEntity.ok(user);

    }

    // Helper method to check if the user is an admin
    private boolean isAdmin(int id) {
        // Fetch the user by ID
        User user = userServiceImp.getUserById(id);

        // Check if the user's role is ADMIN
        return user.getRole().equals("ADMIN");
    }
}
