package com.accolite.pru.health.AuthApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
@ComponentScan(
        basePackages = "com.accolite.pru.health.AuthApp",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.controller\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.service\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.repository\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.security\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.model\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.accolite\\.pru\\.health\\.AuthApp\\.event\\..*")
        }
)
public class MinimalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
        System.out.println("Minimal application started successfully!");
    }
}
