package com.demo.config;


import com.demo.services.MyUserService;
import com.demo.services.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.AuthProvider;

@Configuration
public class SecurityConfig {

    private final MyUserService myUserService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(MyUserService myUserService, JwtAuthFilter jwtAuthFilter) {
        this.myUserService = myUserService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .csrf(AbstractHttpConfigurer::disable)
                        .cors(Customizer.withDefaults())
                        .authorizeHttpRequests(
                                request -> request
                                        .requestMatchers("/h2-console/**").permitAll()
                                        .requestMatchers("/auth/**").permitAll()
                                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                                        .requestMatchers("/user/**").hasAnyAuthority("USER")
                                        .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "USER")
                                        .anyRequest().authenticated()
                        ).sessionManagement(
                                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        ).authenticationProvider(
                                authProvider()
                        ).addFilterBefore(
                                jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                        )
                        .build();
    }

    @Bean
    AuthenticationProvider authProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(myUserService);
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
