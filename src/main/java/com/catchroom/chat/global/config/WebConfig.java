package com.catchroom.chat.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedOrigins("http://localhost:8080")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://localhost:3000")
                 .allowedOrigins("https://dev.dhlbrqe2v28e4.amplifyapp.com")
                .allowedOrigins("http://13.124.240.142:8080")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "MESSAGE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
