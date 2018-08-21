package com.accolite.pru.health.AuthApp.config;

import com.accolite.pru.health.AuthApp.security.JwtAuthenticationEntryPoint;
import com.accolite.pru.health.AuthApp.security.JwtAuthenticationFilter;
import com.accolite.pru.health.AuthApp.security.JwtAuthenticationProvider;
import com.accolite.pru.health.AuthApp.security.JwtSuccessHandler;
import com.accolite.pru.health.AuthApp.service.CustomUserDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.List;

@Profile("!dev")
@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "com.accolite.pru.health.AuthApp.repository")
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private Logger logger;

	private JwtAuthenticationProvider authenticationProvider;

	private JwtAuthenticationEntryPoint jwtEntryPoint;


	@Override
	@Bean
	public AuthenticationManager authenticationManager() {
		List<AuthenticationProvider> authProviders = Collections.singletonList(authenticationProvider);
		return new ProviderManager(authProviders);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter("");
		jwtAuthFilter.setAuthenticationManager(authenticationManager());
		jwtAuthFilter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		return jwtAuthFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoderBean());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/**/secured/**").authenticated()
				.anyRequest().permitAll()
				.and()
				.exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

}
