package com.example.lms.service;

import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {
    @InjectMocks
    private UserServiceImp userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    @BeforeEach
    void setUp() {
        user = new User("User1", "test@gmail.com", "12345678", Role.ADMIN);
    }
    @Test
    void createUserSuccessfully() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals("User1", createdUser.getName());
    }

    @Test
    void getUserByEmailSuccessfully() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.of(user));
        User foundUser = userService.getUserByEmail(user.getEmail());
        assertNotNull(foundUser);
        assertEquals("User1", foundUser.getName());
    }

    @Test
    void updateByIdSuccessfully() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));
        user.setName("Updated_User");
        userService.updateById(1, user);
        assertEquals("Updated_User", user.getName());
    }

    @Test
    void deleteByIdSuccessfully() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));
        userService.deleteById(1);
        assertFalse(userRepository.findByEmail(user.getEmail()).isPresent());


    }
    @Test
    void getUserByIdSuccessfully() {

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(1);
        assertNotNull(foundUser);
        assertEquals("User1", foundUser.getName());
        assertEquals("test@gmail.com", foundUser.getEmail());

    }
}