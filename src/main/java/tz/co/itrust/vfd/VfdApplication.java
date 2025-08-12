package tz.co.itrust.vfd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"tz.co.itrust.vfd"})
public class VfdApplication {

    public static void main(String[] args) {
        SpringApplication.run(VfdApplication.class, args);
    }
} 