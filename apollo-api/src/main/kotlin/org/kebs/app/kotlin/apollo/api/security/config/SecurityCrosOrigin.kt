package org.kebs.app.kotlin.apollo.api.security.config

import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.Duration
import java.util.*


@Configuration
class SecurityCrosOrigin(
        private val authenticationProperties: AuthenticationProperties,
) {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(*authenticationProperties.requiresNoAuthenticationCros?.split(",")?.toTypedArray()
                                ?: arrayOf(""))
            }
        }
    }

}