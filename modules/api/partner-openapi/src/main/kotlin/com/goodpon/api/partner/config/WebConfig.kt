package com.goodpon.api.partner.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.partner.idempotency.IdempotencyInterceptor
import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val idempotencyUseCase: IdempotencyUseCase,
    private val objectMapper: ObjectMapper,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        val idempotencyInterceptor = IdempotencyInterceptor(idempotencyUseCase, objectMapper)

        registry.addInterceptor(idempotencyInterceptor)
            .addPathPatterns("/**")
    }
}