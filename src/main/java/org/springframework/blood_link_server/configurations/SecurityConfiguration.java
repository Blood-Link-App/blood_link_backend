package org.springframework.blood_link_server.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.blood_link_server.models.enumerations.UserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    // private final UserDetailsService userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/signUp").permitAll()
                        .requestMatchers("/api/v1/auth/logIn").permitAll()
                        .requestMatchers("/api/v1/donor/**").hasRole(DONOR.name())
                        .requestMatchers("/api/blood-request/create").hasRole(DOCTOR.name())
                        .requestMatchers("/api/v1/bank-stock/**").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/bloodbank/initialize-blood-bank-stocks").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/bank-stock/update-total-quantity").hasRole(BLOODBANK.name())
                        //.requestMatchers("/api/blood-request/create1").hasRole(UserRole.DOCTOR.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
