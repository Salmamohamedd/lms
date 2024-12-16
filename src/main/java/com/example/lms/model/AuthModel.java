package com.example.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class AuthModel {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
