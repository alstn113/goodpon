package com.goodpon.api.dashboard.security

import com.goodpon.core.application.account.AccountService
import com.goodpon.infra.security.AccountVerifiedFilter
import com.goodpon.infra.security.jwt.JwtAuthenticationFilter
import com.goodpon.infra.security.jwt.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val accountService: AccountService,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: JwtAccessDeniedHandler,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val jwtAuthenticationFilter = JwtAuthenticationFilter(
            jwtTokenProvider = jwtTokenProvider,
            accountService = accountService,
            authenticationEntryPoint = authenticationEntryPoint,
        )

        val accountVerifierFilter = AccountVerifiedFilter(
            allowListPaths = listOf(
                "/api/v1/auth/login",
                "/api/v1/auth/verify",
                "/api/v1/auth/verify/resend",
                "/api/v1/account/sign-up",
                "/api/v1/account",
            )
        )

        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/v1/account/sign-up",
                        "/api/v1/auth/login",
                        "/api/v1/auth/verify",
                        "/api/v1/auth/verify/resend",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(accountVerifierFilter, JwtAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:5173")
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