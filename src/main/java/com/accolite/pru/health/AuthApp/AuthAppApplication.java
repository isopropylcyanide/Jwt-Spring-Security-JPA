package com.accolite.pru.health.AuthApp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;


@SpringBootApplication
public class AuthAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthAppApplication.class, args);
	}

	@Bean
	@Scope("prototype")
	Logger logger() {
		return Logger.getLogger(SpringBootApplication.class);
	}
}
