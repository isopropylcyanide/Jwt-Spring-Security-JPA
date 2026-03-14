package com.accolite.pru.health.AuthApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "com.accolite.pru.health.AuthApp",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.controller\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.service\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.security\\..*")
        })
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
        System.out.println("Test application started successfully!");
    }
}