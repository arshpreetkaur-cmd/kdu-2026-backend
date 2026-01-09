package org.example.securecorporatetalentportal.security;

import org.example.securecorporatetalentportal.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Enables @PreAuthorize
public class SecurityConfig {

    //this bean is created for using passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder is the standard secure password hashing encoder
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthFilter jwtAuthFilter,
                                                   JwtAuthEntryPoint entryPoint,
                                                   CustomAccessDeniedHandler deniedHandler) throws Exception {

        http
                // CSRF is mainly for session/cookie-based login. JWT stateless APIs normally disable it.
                .csrf(csrf -> csrf.disable())

                // Setting the session management to STATELESS
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Which endpoints need auth?
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )

                // Custom error responses for 401/403
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)   // 401
                        .accessDeniedHandler(deniedHandler)     // 403
                );

        // Add our JWT filter before Spring's built-in UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
