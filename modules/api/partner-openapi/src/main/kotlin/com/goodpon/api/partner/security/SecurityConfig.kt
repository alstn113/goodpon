package com.goodpon.api.partner.security

import com.goodpon.api.partner.security.filter.ApiKeyAuthenticationFilter
import com.goodpon.api.partner.security.filter.RequestBodyCachingFilter
import com.goodpon.application.partner.merchant.port.`in`.AuthenticateMerchantUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticateMerchantUseCase: AuthenticateMerchantUseCase,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val apiKeyAuthenticationFilter = ApiKeyAuthenticationFilter(
            authenticateMerchantUseCase = authenticateMerchantUseCase,
            authenticationEntryPoint = authenticationEntryPoint,
        )
        val requestBodyCachingFilter = RequestBodyCachingFilter()

        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .addFilterAfter(apiKeyAuthenticationFilter, ExceptionTranslationFilter::class.java)
            .addFilterAfter(requestBodyCachingFilter, ApiKeyAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }

        val configurationSource = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration)
        }
        return configurationSource
    }
}