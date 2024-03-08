package com.kiwit.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(Customizer.withDefaults());

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ));

//        http.addFilterBefore(JWT)

//        http.exceptionHandling(exceptionHandling -> exceptionHandling
//                .accessDeniedHandler(new CustomAccessDeniedHandler()));
//        http.exceptionHandling(exceptionHandling -> exceptionHandling
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));


        http.authorizeHttpRequests(authz -> authz
//                .requestMatchers("/api/v1/user/refresh").permitAll()
                .anyRequest().permitAll());

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers();
//    }
}
