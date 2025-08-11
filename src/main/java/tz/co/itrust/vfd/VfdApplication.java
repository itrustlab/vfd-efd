package tz.co.itrust.vfd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Main Spring Boot Application class for iTrust VFD Microservice
 * 
 * This microservice handles Virtual Financial Data (VFD) operations
 * and integrates with external VFD systems for financial data processing.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"tz.co.itrust.vfd"})
public class VfdApplication {

    public static void main(String[] args) {
        SpringApplication.run(VfdApplication.class, args);
    }
    
    /**
     * Provides a RestTemplate bean for HTTP operations
     * @return configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 