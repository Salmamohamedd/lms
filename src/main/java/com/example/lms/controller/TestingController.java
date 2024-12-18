package com.example.lms.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestingController {
    @GetMapping
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public String test(){
        return "hello";
    }
}
