/*
 *
 *  $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 *  $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 *  $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 *  $$$$$$$\ |\$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 *  $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 *  $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 *  $$$$$$$  |\$$$$  |$$ | \$\       \$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 *  \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 *  $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 *  \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *     $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *     $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$\
 *     $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *     $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *     $$ |   $$$$$$$$\ \$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$  |$$$$$$\ $$$$$$$$\ \$$$$  |
 *     \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *    Copyright (c) 2020.  BSK
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package org.kebs.app.kotlin.apollo.api.flux.security.config

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.api.flux.security.jwt.authorization.JwtRoleAuthorizationManager
import org.kebs.app.kotlin.apollo.api.flux.security.service.CustomUserDetailsService
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Configuration

@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
class WebConfig(
    private val authenticationProperties: AuthenticationProperties

) : WebFluxConfigurer {
    private final var exclusions: Array<String> = authenticationProperties.requiresNoAuthentication?.split(",")?.toTypedArray() ?: arrayOf("")


    @Bean
    fun configureSecurity(
        http: ServerHttpSecurity,
        jwtAuthenticationFilter: AuthenticationWebFilter,
        jwtRoleAuthorizationManager: JwtRoleAuthorizationManager
    ): SecurityWebFilterChain {
        return http
//            .requiresChannel().anyRequest().requiresSecure()
//            .and()
            .csrf().disable()
            .logout().disable()
            .authorizeExchange()
            .pathMatchers(*exclusions).permitAll()
            .anyExchange().access(jwtRoleAuthorizationManager)
            .and()
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .exceptionHandling()
            .authenticationEntryPoint { swe: ServerWebExchange, e: AuthenticationException? ->
                Mono.fromRunnable {
                    KotlinLogging.logger { }.error("${e?.message} on ${swe.request.uri}")
                    swe.response.statusCode = HttpStatus.UNAUTHORIZED
                }

            }
            .accessDeniedHandler { swe: ServerWebExchange, e: AccessDeniedException? ->
                KotlinLogging.logger { }.error("${e?.message} on ${swe.request.uri}")
                Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }
            }
            .and()
            .build()
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun authenticationWebFilter(
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        jwtConverter: ServerAuthenticationConverter,
        serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
        jwtServerAuthenticationFailureHandler: ServerAuthenticationFailureHandler,
        jwtService: JwtTokenService
    ): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(reactiveAuthenticationManager)
        authenticationWebFilter.setRequiresAuthenticationMatcher { ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, authenticationProperties.apiLoginUrl).matches(it) }
        authenticationWebFilter.setServerAuthenticationConverter(jwtConverter)
        authenticationWebFilter.setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler)
        authenticationWebFilter.setAuthenticationFailureHandler(jwtServerAuthenticationFailureHandler)
//        authenticationWebFilter.setSecurityContextRepository(securityContextRepository)
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        return authenticationWebFilter
    }


    @Bean
    fun reactiveAuthenticationManager(
        reactiveUserDetailsService: CustomUserDetailsService,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager {
        val manager = UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService)
        manager.setPasswordEncoder(passwordEncoder)
        return manager
    }


}

