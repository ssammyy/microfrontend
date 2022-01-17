package org.kebs.app.kotlin.apollo.api.security.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper

import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer



@Configuration
class SecurityCrosOrigin(
        private val authenticationProperties: AuthenticationProperties,
) {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
                val objectMapper = ObjectMapper()
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                val jsonConverter = MappingJackson2HttpMessageConverter(objectMapper)
                converters.add(jsonConverter)
            }

            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(*authenticationProperties.requiresNoAuthenticationCros?.split(",")?.toTypedArray()
                                ?: arrayOf(""))
            }
        }
    }

}