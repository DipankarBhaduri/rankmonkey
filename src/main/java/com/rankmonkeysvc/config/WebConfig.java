package com.rankmonkeysvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static com.rankmonkeysvc.messages.StaticMessages.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping(CORS_MAPPING)
                .allowedOrigins(ASSESSMENT_PLATFORM_BASE_URL, ADMIN_BASE_URL)
                .allowedMethods(ALLOWED_METHODS.split(", "))
                .allowedHeaders(ALLOWED_HEADERS)
                .allowCredentials(ALLOW_CREDENTIALS)
                .maxAge(MAX_AGE);
    }
}