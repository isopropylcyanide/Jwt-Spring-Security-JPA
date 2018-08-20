package com.accolite.pru.health.AuthApp;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		AuthAppApplication.class,
		Jsr310JpaConverters.class
})
public class AuthAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthAppApplication.class, args);
	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Bean
	@Scope("prototype")
	Logger logger() {
		return Logger.getLogger(SpringBootApplication.class);
	}
}
