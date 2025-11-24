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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
                .authorizeHttpRequests(auth -> auth



                        .requestMatchers("/api/v1/auth/signUp").permitAll()
                        .requestMatchers("/api/v1/auth/logIn").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/api/v1/user/**").hasAnyRole(DOCTOR.name(), BLOODBANK.name(), DONOR.name())

/*=============================================================================Specific questions to donors============================================================================*/


                        .requestMatchers("/api/v1/donor/**").hasRole(DONOR.name())
                        .requestMatchers("/api/v1/medical-profile/create-profile").hasRole(DONOR.name())
                        .requestMatchers("/api/v1/medical-profile/get-profile").hasRole(DONOR.name())
                        .requestMatchers("/api/v1/medical-profile/update-profile").hasRole(DONOR.name())
                                .requestMatchers("/api/v1/donor-response/create-donor-response").hasRole(DONOR.name())
                                .requestMatchers("/api/v1/donation-request/create-donation-request").hasRole(DONOR.name())
                                .requestMatchers("/api/v1/donor/get-donation-requests").hasRole(DONOR.name())


/*=============================================================================Specific questions to doctors============================================================================*/


                                .requestMatchers("/api/blood-request/create").hasRole(DOCTOR.name())


/*=============================================================================Specific questions to blood banks============================================================================*/


                                .requestMatchers("/api/blood-request/get-pending-bloodRequests-by-bloodbank/").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/blood-request/process-request/**").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/bank-stock/**").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/bloodbank/initialize-blood-bank-stocks").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/bank-stock/update-total-quantity").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/bank-stock/increase/").hasRole(BLOODBANK.name())
                        .requestMatchers("/api/v1/alert/create-alert/**").hasRole(BLOODBANK.name())
                                .requestMatchers("/api/v1/donation-request/process-donation-request").hasRole(BLOODBANK.name())

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
