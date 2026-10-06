package com.example.refund.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf()
            .disable()

            .authorizeRequests()
            .antMatchers("/rc/api/v1/refunds")
            .hasAnyRole("MERCHANT", "SERVICE")
            .anyRequest()
            .authenticated()

            .and()
            .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails merchant =
                User.withUsername("merchant")
                    .password(passwordEncoder.encode("merchant123"))
                    .roles("MERCHANT")
                    .build();

        UserDetails service =
                User.withUsername("service")
                    .password(passwordEncoder.encode("service123"))
                    .roles("SERVICE")
                    .build();

        return new InMemoryUserDetailsManager(
                merchant,
                service
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}