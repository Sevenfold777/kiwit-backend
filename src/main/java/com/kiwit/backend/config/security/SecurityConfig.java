package com.kiwit.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(Customizer.withDefaults());

        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/v1/user/refresh",
                        "/api/v1/user/sign-up",
                        "/api/v1/user/sign-in").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().authenticated());


        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new JwtExceptionFilter()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/h2-console/**",
                "/favicon.ico",
                "/error",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**");
    }
}
