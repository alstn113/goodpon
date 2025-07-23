package com.goodpon.api.dashboard.security

import com.goodpon.api.dashboard.security.exception.TokenAccessDeniedHandler
import com.goodpon.api.dashboard.security.filter.AccountVerifiedFilter
import com.goodpon.api.dashboard.security.filter.TokenAuthenticationFilter
import com.goodpon.application.dashboard.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.application.dashboard.auth.port.out.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.ExceptionTranslationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: TokenAccessDeniedHandler,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val tokenAllowListPatterns = listOf(
            "/v1/accounts/sign-up",
            "/v1/auth/login",
            "/v1/auth/verify",
            "/v1/auth/verify/resend",
            "/api-docs/**",
        )
        val verifiedAllowListPatterns = tokenAllowListPatterns + listOf(
            "/v1/accounts",
        )

        val tokenAuthenticationFilter = TokenAuthenticationFilter(
            tokenProvider = tokenProvider,
            getAccountInfoUseCase = getAccountInfoUseCase,
            authenticationEntryPoint = authenticationEntryPoint,
            allowListPatterns = tokenAllowListPatterns,
        )
        val accountVerifierFilter = AccountVerifiedFilter(
            allowListPatterns = verifiedAllowListPatterns
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
                    .requestMatchers(* tokenAllowListPatterns.toTypedArray()).permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }
            // 예외 처리를 위해 ExceptionTranslationFilter 뒤에 Custom Filter 들을 위치시킴
            .addFilterAfter(tokenAuthenticationFilter, ExceptionTranslationFilter::class.java)
            .addFilterAfter(accountVerifierFilter, tokenAuthenticationFilter::class.java)

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