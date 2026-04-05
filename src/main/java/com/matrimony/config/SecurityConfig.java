package com.matrimony.config;

import com.matrimony.security.jwt.AuthEntryPointJwt;
import com.matrimony.security.jwt.AuthTokenFilter;
import com.matrimony.security.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {

        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors().and().csrf().disable()

            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(accessDeniedHandler)
            )

            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(authz -> authz

                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/users/**").permitAll()
                    .requestMatchers("/api/created-for/**").permitAll()
                    .requestMatchers("/api/castes/**").permitAll()

                    .requestMatchers("/api/mother-tongues/**").permitAll()
                    .requestMatchers("/api/sub-caste/**").permitAll()
                    .requestMatchers("/api/heights/**").permitAll()
                    .requestMatchers("/api/gothras/**").permitAll()

                    .requestMatchers("/api/annual-income/**").permitAll()

                    .requestMatchers("/api/country/**").permitAll()
                    .requestMatchers("/api/state/**").permitAll()
                    .requestMatchers("/api/district/**").permitAll()
                    .requestMatchers("/api/weight/kgs/**").permitAll()

                    .requestMatchers("/api/occupation/**").permitAll()
                    .requestMatchers("/api/education/**").permitAll()

                    .requestMatchers("/api/stars/**").permitAll()
                    .requestMatchers("/api/raasi/**").permitAll()

                    .requestMatchers("/api/public/**").permitAll()

                    .requestMatchers("/ws/**").permitAll()
                    .requestMatchers("/chat.html").permitAll()
                    .requestMatchers("/static/**").permitAll()

                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                    .requestMatchers("/api/admin/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
            );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}