package com.imagica.guest_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Guest Service API")
                        .version("1.0")
                        .description("API documentation for the Guest Service in Great Escape Project"));
    }
}


//URL for swagger - http://localhost:8081/swagger-ui/index.html