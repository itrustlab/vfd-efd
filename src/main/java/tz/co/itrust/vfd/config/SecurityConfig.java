package tz.co.itrust.vfd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
                    .antMatchers("/health").permitAll()
                    .antMatchers("/status").permitAll()
                    .antMatchers("/info").permitAll()
                    .antMatchers("/test").permitAll()
                    .antMatchers("/receipt").permitAll()
                    .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);

        return http.build();
    }
} 