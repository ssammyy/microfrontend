package com.ngumo.inventoryapi.Configurations.Security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    jwtAuthFilter: JwtAuthFilter,
) : WebSecurityConfigurerAdapter() {

    val customFilter = jwtAuthFilter


    override fun configure(http: HttpSecurity) {
        http.cors()
                .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/authenticate")
//            .antMatchers("/**/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }





        @Bean
        fun authenticationProvider(): AuthenticationProvider {
            val daoAuthenticationProvider = DaoAuthenticationProvider()
            daoAuthenticationProvider.setUserDetailsService(userDetailsService())
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
            return daoAuthenticationProvider
        }



    @Bean
    fun passwordEncoder(): PasswordEncoder? {
//        return BCryptPasswordEncoder()

        return NoOpPasswordEncoder.getInstance()
    }







}
