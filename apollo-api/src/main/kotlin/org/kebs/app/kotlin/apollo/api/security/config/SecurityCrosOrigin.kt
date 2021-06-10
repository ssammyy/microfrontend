package org.kebs.app.kotlin.apollo.api.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer



@Configuration
class SecurityCrosOrigin {

        @Bean
        fun corsConfigurer(): WebMvcConfigurer {
            return object : WebMvcConfigurer {
                override fun addCorsMappings(registry: CorsRegistry) {
                    registry.addMapping("/**").allowedOrigins("http://localhost:4200")
                }
            }
        }

}