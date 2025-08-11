package tz.co.itrust.vfd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for VFD Microservice
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                .antMatchers("/api/vfd/health").permitAll()
                .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer()
                .jwt();
        
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
} 