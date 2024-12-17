package com.example.lms.config;

import com.example.lms.service.CustomUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserInfoService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // there is no session on the server side (stateless), CSRF protection doesn't apply
                .authorizeHttpRequests(request -> request.requestMatchers("/login", "/register").permitAll() // Allows unrestricted (public) access to the /login and /register endpoints.
                        .anyRequest().authenticated()) // Ensures that every other request to the server requires authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults()) // HTTP Basic Authentication
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set the custom UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Use BCryptPasswordEncoder
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder to securely hash passwords
        return new BCryptPasswordEncoder();
    }



}
