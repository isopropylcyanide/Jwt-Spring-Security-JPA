/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.accolite.pru.health.AuthApp.config;

import com.accolite.pru.health.AuthApp.security.JwtAuthenticationEntryPoint;
import com.accolite.pru.health.AuthApp.security.JwtAuthenticationFilter;
import com.accolite.pru.health.AuthApp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;
    private final Environment environment;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Check if we're running in a dev profile
        boolean isDevProfile = isDevProfile();

        if (isDevProfile) {
            // Development configuration - permit all requests
            if (Arrays.asList(environment.getActiveProfiles()).contains("postgresql")) {
                log.info("Loaded inside postgresql profile");
            } else {
                log.info("Loaded inside a dev only");
            }

            http.authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests.anyRequest().permitAll()
            );
        } else {
            // Production configuration with JWT security
            http
                    .cors(cors -> cors.configure(http))
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exceptionHandling ->
                            exceptionHandling.authenticationEntryPoint(jwtEntryPoint)
                    )
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui", "/v3/api-docs").permitAll()
                                    .anyRequest().authenticated()
                    );

            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        return Arrays.stream(activeProfiles)
                .anyMatch(profile -> profile.startsWith("dev"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
