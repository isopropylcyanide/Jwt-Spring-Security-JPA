package com.accolite.pru.health.AuthApp.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Profile("dev")
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityDevConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = Logger.getLogger(WebSecurityDevConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Loaded inside a dev only");
        http.authorizeRequests()
                .anyRequest().permitAll();
    }
}
