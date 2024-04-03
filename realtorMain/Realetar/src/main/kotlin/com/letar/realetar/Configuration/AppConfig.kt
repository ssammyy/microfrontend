package com.letar.realetar.Configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AppConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Set the CORS configuration for all endpoints
            .allowedOrigins("*") // Set your allowed origin(s) here, "*" allows all origins (not recommended for production)
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Set the HTTP methods you want to allow
            .allowedHeaders("*") // Set the allowed request headers, "*" allows all headers
            .allowCredentials(false) // Allow credentials such as cookies, if applicable
            .maxAge(3600) // Set the max age of the CORS preflight request in seconds (optional)
    }

}
