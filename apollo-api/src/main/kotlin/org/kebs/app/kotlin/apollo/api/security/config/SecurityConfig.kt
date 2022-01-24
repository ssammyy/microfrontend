package org.kebs.app.kotlin.apollo.api.security.config

import mu.KotlinLogging
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.security.filters.ApiClientAuthorizationFilter
import org.kebs.app.kotlin.apollo.api.security.filters.CustomUsernamePasswordAuthenticationFilter
import org.kebs.app.kotlin.apollo.api.security.filters.JWTAuthorizationFilter
import org.kebs.app.kotlin.apollo.api.security.handlers.CustomAccessDeniedHandler
import org.kebs.app.kotlin.apollo.api.security.handlers.CustomAuthenticationFailureHandler
import org.kebs.app.kotlin.apollo.api.security.handlers.CustomLogoutSuccessHandler
import org.kebs.app.kotlin.apollo.api.security.handlers.RefererAuthenticationSuccessHandler
import org.kebs.app.kotlin.apollo.api.security.service.ApiClientAuthenticationProvider
import org.kebs.app.kotlin.apollo.api.security.service.CustomUserDetailsService
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IApprovalStatusRepository
import org.kebs.app.kotlin.apollo.store.repo.IStatusValuesRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserProfilesRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(private val customUserDetailsService: CustomUserDetailsService){
    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun usersAuthProvider(): DaoAuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        authenticationProvider.setUserDetailsService(customUserDetailsService)
        return authenticationProvider;
    }

    @Configuration
    @Order(1)
    class TokenSecurityConfigurationAdapter(
            private val clientFilter: ApiClientAuthorizationFilter,
            private val customUserDetailsService: CustomUserDetailsService,
            private val authenticationProperties: AuthenticationProperties,
            private val daoAuthenticationProvider: DaoAuthenticationProvider,
            private val apiClientProvider: ApiClientAuthenticationProvider
    ) : WebSecurityConfigurerAdapter() {

        private var exclusions: Array<String> =
                authenticationProperties.requiresNoAuthenticationApolloApiToken?.split(",")?.toTypedArray()
                        ?: arrayOf("")
        private var exclusionsCros: Array<String> =
                authenticationProperties.requiresNoAuthenticationCros?.split(",")?.toTypedArray() ?: arrayOf("")

        @Bean
        fun authenticationTokenFilterBean(): JWTAuthorizationFilter {
            return JWTAuthorizationFilter()
        }

        @Throws(Exception::class)
        override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
            // Used for user account authentication
            authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider)
        }

        @Bean
        fun corsConfigurationSource(): CorsConfigurationSource? {
            val configuration = CorsConfiguration()
            // MOVE TO CONFIGURATION FILE
            configuration.allowedOrigins = listOf(*exclusionsCros)
            configuration.allowedHeaders =
                    Arrays.asList(
                        "Origin",
                        "Access-Control-Allow-Origin",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "Origin,Accept",
                        "X-Requested-With",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "enctype"
                    )
            configuration.exposedHeaders = Arrays.asList(
                    "Origin", "Content-Type", "Accept", "Authorization",
                    "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"
            )
            configuration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
            configuration.allowCredentials = true
            val source = UrlBasedCorsConfigurationSource()
            source.registerCorsConfiguration("/**", configuration)
            return source
        }

        override fun configure(http: HttpSecurity) {
            http
                    .cors().configurationSource(corsConfigurationSource())
                    .and()
                    .csrf().disable()
                    .antMatcher("/api/v1/**")
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    /**
                     * TODO: Move to external configuration
                     */
                    .antMatchers(*exclusions)
                    .permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .accessDeniedHandler { _, response, accessDeniedException ->
                        response?.status = HttpStatus.FORBIDDEN.value()
                        response?.outputStream?.println("message: access denied to perform this actions, please consult your administrator")
                    }
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)
            http.addFilterAfter(clientFilter, UsernamePasswordAuthenticationFilter::class.java)
        }
    }

    @Configuration
    @Order(2)
    class FormLoginWebSecurityConfigurationAdapter(
            private val authenticationProperties: AuthenticationProperties,
            private val customUserDetailsService: CustomUserDetailsService,
            private val usersRepo: IUserRepository,
            private val approvalStatusRepo: IApprovalStatusRepository,
            private val usersProfilesRepository: IUserProfilesRepository,
            private val statusValuesRepo: IStatusValuesRepository,
            private val taskService: TaskService,
            private val applicationMapProperties: ApplicationMapProperties,
            private val passwordEncoder: PasswordEncoder,
            private val apiClientProvider: ApiClientAuthenticationProvider
    ) : WebSecurityConfigurerAdapter(

    ) {

        private var exclusions: Array<String> =
                authenticationProperties.requiresNoAuthenticationApolloApi?.split(",")?.toTypedArray() ?: arrayOf("")

        @Bean
        fun accessDeniedHandler(): CustomAccessDeniedHandler = CustomAccessDeniedHandler()


        @Throws(Exception::class)
        override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
            authenticationManagerBuilder
                    .userDetailsService(customUserDetailsService)
                    .passwordEncoder(passwordEncoder)
            // Used for client authentication
            authenticationManagerBuilder.authenticationProvider(apiClientProvider)
        }

        @Bean
        override fun authenticationManager(): AuthenticationManager {
            return super.authenticationManager()
        }


        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {
            http
//                .requiresChannel().anyRequest().requiresSecure()
//                .and()
                    .httpBasic().disable()
                    .cors().disable()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(*exclusions).permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
                    .formLogin()
                    .loginPage("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/auth/login-service")
                    .successHandler(loginSuccessHandler(authenticationProperties.homePage))
                    .failureUrl("/auth/login?error")
                    //.permitAll()
//            .failureHandler(authenticationFailureHandler())
//            .and()
//            .logout()
//            .deleteCookies("JSESSIONID")
//            .logoutUrl(authenticationProperties.logoutUrl)
//            .logoutSuccessHandler(logoutSuccessHandler())
                    .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(
                            mainAuthenticationEntryPoint("/auth/login"),
                            AntPathRequestMatcher("/")
                    )
                    .accessDeniedPage("/accessDenied")
                    .accessDeniedHandler(accessDeniedHandler())


        }

        fun loginSuccessHandler(url: String?): RefererAuthenticationSuccessHandler =
                RefererAuthenticationSuccessHandler(
                        usersRepo = usersRepo,
                        usersProfilesRepository = usersProfilesRepository,
                        approvalStatusRepo = approvalStatusRepo,
                        taskService = taskService,
                        url = url,
                        statusValuesRepo = statusValuesRepo,
                        applicationMapProperties = applicationMapProperties
                )

        @Bean
        fun logoutSuccessHandler(): CustomLogoutSuccessHandler? = CustomLogoutSuccessHandler()

        @Bean
        fun authenticationFailureHandler(): CustomAuthenticationFailureHandler = CustomAuthenticationFailureHandler()


        fun mainAuthenticationEntryPoint(url: String): AuthenticationEntryPoint = LoginUrlAuthenticationEntryPoint(url)


        @Bean
        fun authenticationFilter(): CustomUsernamePasswordAuthenticationFilter {
            KotlinLogging.logger { }.trace("Entering filter")
            return CustomUsernamePasswordAuthenticationFilter().apply {
                setAuthenticationManager(authenticationManager())
                setFilterProcessesUrl("/auth/login-service")
                setAuthenticationSuccessHandler(loginSuccessHandler(authenticationProperties.homePage))
                setAuthenticationFailureHandler(failureHandler())
            }
        }

        fun failureHandler(): SimpleUrlAuthenticationFailureHandler? {
            return SimpleUrlAuthenticationFailureHandler("/auth/login?error=true")
        }


    }
}
