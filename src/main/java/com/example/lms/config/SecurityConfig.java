package com.example.lms.config;


import com.example.lms.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/students/**").hasAnyRole(Role.STUDENT.name(), Role.ADMIN.name(), Role.INSTRUCTOR.name())
                .requestMatchers("/api/instructors/**").hasAnyRole(Role.ADMIN.name(), Role.INSTRUCTOR.name())
                .requestMatchers("/api/admins/**").hasAnyRole(Role.ADMIN.name())
                .anyRequest().authenticated())

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session

                .exceptionHandling(
                        e -> e.authenticationEntryPoint(
                                (request, response, authException) -> response.setStatus(401)
                        )
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before UsernamePasswordAuthenticationFilter

//        return http.csrf(csrf -> csrf.disable()) // there is no session on the server side (stateless), CSRF protection doesn't apply
//                .authorizeHttpRequests()
//                .requestMatchers("/login", "/register").permitAll() // Allows unrestricted (public) access to the /login and /register endpoints.
////                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restricted to ADMIN
////                        .requestMatchers("/instructor/**").hasRole("INSTRUCTOR") // Restricted to INSTRUCTOR
////                        .requestMatchers("/student/**").hasRole("STUDENT") // Restricted to STUDENT
//                        .anyRequest().permitAll()
//                 // Ensures that every other request to the server requires authentication
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .httpBasic(Customizer.withDefaults()) // HTTP Basic Authentication
//                .build();
        return http.build();
    }








}
