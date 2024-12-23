package com.example.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Role role; // Role: ADMIN, INSTRUCTOR, STUDENT

    public User(String name, String email, String password,Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    @Column(name = "email_notifications", nullable = false)
    private boolean emailNotifications = true;  // Default to true (can be changed by the user)

    @Column(name = "student_enrollments", nullable = false)
    private boolean studentEnrollments = true;  // Default to true (can be changed by the user)

    @Column(name = "course_updates", nullable = false)
    private boolean courseUpdates = true;  // Default to true (can be changed by the user)

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isStudentEnrollments() {
        return studentEnrollments;
    }

    public void setStudentEnrollments(boolean studentEnrollments) {
        this.studentEnrollments = studentEnrollments;
    }

    public boolean isCourseUpdates() {
        return courseUpdates;
    }

    public void setCourseUpdates(boolean courseUpdates) {
        this.courseUpdates = courseUpdates;
    }

    // Existing getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
