/*
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$  |$$ | \$\       \$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$  |$$$$$$\ $$$$$$$$\ \$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.kebs.app.kotlin.apollo.standardsdevelopment.security.config

import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.standardsdevelopment.security.authorization.JWTReactiveAuthorizationFilter
import org.kebs.app.kotlin.apollo.standardsdevelopment.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.standardsdevelopment.security.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
class SecurityConfiguration(
    private val authenticationProperties: AuthenticationProperties
) : WebFluxConfigurer {
    private var exclusions: Array<String> = authenticationProperties.requiresNoAuthentication?.split(",")?.toTypedArray() ?: arrayOf("")
    private var postExclusions: Array<String> = authenticationProperties.requiresNoAuthenticationPost?.split(",")?.toTypedArray() ?: arrayOf("")


    @Bean
    fun configureSecurity(
        http: ServerHttpSecurity,
        jwtAuthenticationFilter: AuthenticationWebFilter,
        tokenService: JwtTokenService,
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
    ): SecurityWebFilterChain {

        return http
            .addFilterAt(CorsFilter(), SecurityWebFiltersOrder.FIRST)
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(JWTReactiveAuthorizationFilter(tokenService, authenticationProperties), SecurityWebFiltersOrder.AUTHORIZATION)
            .csrf().disable()
            .logout().disable()
            .authorizeExchange {
                it
//                    .pathMatchers(HttpMethod.GET, *exclusions).permitAll()
                    .pathMatchers(HttpMethod.GET, *exclusions).permitAll()
                    .pathMatchers(HttpMethod.POST, *postExclusions).permitAll()
                    .anyExchange().authenticated()
            }
            .httpBasic { }
            .exceptionHandling()
            .authenticationEntryPoint { swe: ServerWebExchange, e: Exception? ->
                mono {
                    KotlinLogging.logger { }.error("${e?.message} on ${swe.request.uri}")
                    KotlinLogging.logger { }.debug("${e?.message} on ${swe.request.uri}", e)
                    swe.response.statusCode = HttpStatus.UNAUTHORIZED
                    null
                }

            }
            .accessDeniedHandler { swe: ServerWebExchange, e: AccessDeniedException? ->
                mono {
                    KotlinLogging.logger { }.error("${e?.message} on ${swe.request.uri}")
                    KotlinLogging.logger { }.debug("${e?.message} on ${swe.request.uri}", e)
                    swe.response.statusCode = HttpStatus.FORBIDDEN
                    null

                }

            }
            .and()
            .build()

    }


    /**
     * ![AuthenticationWebFilter Flow](/diagrams/authentication_flow.png?raw=true "AuthenticationWebFilter Flow")

    1. Checks if the request match a given pattern (any by default). This is done by [ServerWebExchangeMatcher](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/util/matcher/ServerWebExchangeMatcher.java).
    If success, continue with step 2, if not skip this filter and continue the chain.
    2. Converts the request to an unauthenticated Authentication object (from the Authorization header by default). This is done by [ServerAuthenticationConverter](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/authentication/ServerAuthenticationConverter.java).
    If the converter returns an empty Mono, continue the chain otherwise go to step 3.
    3. Verify the Authentication object provided by step 2. This step is done by [ReactiveAuthenticationManager](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/ReactiveAuthenticationManager.java). If the verification is not successful (an AuthenticationException occurs) execute [ServerAuthenticationFailureHandler](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/authentication/ServerAuthenticationFailureHandler.java) (step 5), otherwise go step 4.
    4. On authentication success:
    1. Save the Authentication object in the security context (session) (nothing is saved by default). By [ServerSecurityContextRepository](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/context/ServerSecurityContextRepository.java).
    2. Execute [ServerAuthenticationSuccessHandler](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/authentication/ServerAuthenticationSuccessHandler.java) (continue the chain by default).
    5. On authentication error:
    1. Execute [ServerAuthenticationFailureHandler](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/authentication/ServerAuthenticationFailureHandler.java) (prompts a user for HTTP Basic authentication by default).

    This is the general algorithm that AuthenticationWebFilter follows, and in which we can customize all steps or keep the default ones that are handy for us.
    In our case, the steps that we are gonna replace are:

    1. We want to authenticate users through a POST to `/login` endpoint, our matcher looks at the request and see if this pattern match. We can use the factory method `pathMatchers()` that [ServerWebExchangeMatchers](/backend-server/src/main/kotlin/com/popokis/backend_server/application/WebConfig.kt#L71) provides
    to create our custom matcher.
    2. Our converter gets from the body a JSON with `username` and `password` attributes and creates an unauthenticated Authentication object with them. Done by [@link JWTConverter].
    3. [@see AbstractUserDetailsReactiveAuthenticationManager] gets the principal (username) and the credentials (password) from the Authentication object created in step 2 and:
    1. [@libk AbstractUserDetailsReactiveAuthenticationManager], if exists go to step 3.ii, otherwise throw BadCredentialsException and executes ServerAuthenticationFailureHandler (step 5).
    2. [AbstractUserDetailsReactiveAuthenticationManager](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/AbstractUserDetailsReactiveAuthenticationManager.java#L10
        3) checks if passwords match, if so authentication success, if not throw BadCredentialsException and executes ServerAuthenticationFailureHandler (step 5).
    4. On authentication success:
    1. Our project is just an HTTP API and by default should be stateless, then we don't want to create a session so skip it. Done by [NoOpServerSecurityContextRepository](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/context/NoOpServerSecurityContextRepository.java).
    2. Execute our [JWTServerAuthenticationSuccessHandler](/backend-server/src/main/kotlin/com/popokis/backend_server/application/security/authentication/JWTServerAuthenticationSuccessHandler.kt) that generates an access and a refresh token and put them in the header of the response.
    5. On authentication error:
    1. Return unauthorized error. Done by [JWTServerAuthenticationFailureHandler](/backend-server/src/main/kotlin/com/popokis/backend_server/application/JWTServerAuthenticationFailureHandler.kt).

    We are following these steps customizing [AuthenticationWebFilter](/backend-server/src/main/kotlin/com/popokis/backend_server/application/WebConfig.kt#L70), and our ServerHttpSecurity configuration looks like:
     */
    @Bean
    fun authenticationWebFilter(
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        jwtConverter: ServerAuthenticationConverter,
        serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
        jwtServerAuthenticationFailureHandler: ServerAuthenticationFailureHandler
    ): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(reactiveAuthenticationManager)
        authenticationWebFilter.setRequiresAuthenticationMatcher { ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, authenticationProperties.apiLoginUrl).matches(it) }
        authenticationWebFilter.setServerAuthenticationConverter(jwtConverter)
        authenticationWebFilter.setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler)
        authenticationWebFilter.setAuthenticationFailureHandler(jwtServerAuthenticationFailureHandler)
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


    class CorsFilter : WebFilter {
        override fun filter(ctx: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
            ctx.response.headers.add("Access-Control-Allow-Origin", "*")
            ctx.response.headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
            ctx.response.headers.add("Access-Control-Allow-Headers", HEADERS)
            return if (ctx.request.method == HttpMethod.OPTIONS) {
                ctx.response.headers.add("Access-Control-Max-Age", "1728000")
                ctx.response.statusCode = HttpStatus.NO_CONTENT
                Mono.empty()
            } else {
                ctx.response.headers.add("Access-Control-Expose-Headers", HEADERS)
                chain.filter(ctx)
            }
        }

        companion object {
            const val HEADERS = "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With," +
                    "If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range,Authorization"
        }

    }
}


/**
 * Useful for mastering how the reactive context works
 */
@Suppress("unused")
class JWTWebFilter(
    private val authenticationManager: ReactiveAuthenticationManager
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { securityContext ->
                this.authenticationManager.authenticate(securityContext.authentication)
                    .map { securityContext.authentication = it }
                    .map { exchange }
            }
            .defaultIfEmpty(exchange)
            .flatMap { chain.filter(it) }
    }
}
