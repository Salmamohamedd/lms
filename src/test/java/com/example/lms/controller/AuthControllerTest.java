package com.example.lms.controller;

import com.example.lms.config.JwtService;
import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;




    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
   void testRegister_UserAlreadyExists() {
        User user = new User("USER1", "test@example.com", "1234", Role.STUDENT);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.register(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
     void testRegister_Success() {
        User user = new User("USER1", "test@example.com", "1234", Role.STUDENT);

        ResponseEntity<?> response = authController.register(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertTrue(responseBody.containsKey("token"));
        assertTrue(responseBody.containsKey("user"));
    }

    @Test
    void testLogin_UserNotFound() {
        User user = new User("USER1", "test@example.com", "1234", Role.STUDENT);

        ResponseEntity<?> response = authController.login(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }


}