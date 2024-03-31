package com.kiwit.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securityJWTName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJWTName);
        Components components = new Components()
                .addSecuritySchemes(securityJWTName, new SecurityScheme()
                        .name(securityJWTName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat(securityJWTName));

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);

    }


}
