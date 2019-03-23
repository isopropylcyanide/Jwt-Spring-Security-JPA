package com.accolite.pru.health.AuthApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;


@SpringBootApplication
@EntityScan(basePackageClasses = {
        AuthAppApplication.class,
        Jsr310JpaConverters.class
})
public class AuthAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthAppApplication.class, args);
    }

}
