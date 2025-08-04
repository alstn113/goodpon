package com.goodpon.api.partner.config

import com.goodpon.api.partner.interceptor.IdempotencyInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val idempotencyInterceptor: IdempotencyInterceptor,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(idempotencyInterceptor)
            .addPathPatterns("/**")
    }
}