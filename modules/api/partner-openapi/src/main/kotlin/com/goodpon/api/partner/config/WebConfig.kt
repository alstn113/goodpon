package com.goodpon.api.partner.config

import com.goodpon.api.partner.interceptor.IdempotencyInterceptor
import com.goodpon.api.partner.interceptor.TraceIdInterceptor
import com.goodpon.api.partner.interceptor.TraceIdProvider
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val traceIdProvider: TraceIdProvider,
    private val idempotencyInterceptor: IdempotencyInterceptor,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        val traceIdInterceptor = TraceIdInterceptor(traceIdProvider)

        // 응답 시 trace id를 헤더에 넣어주기 위한 순서
        registry.addInterceptor(traceIdInterceptor).addPathPatterns("/**")
        registry.addInterceptor(idempotencyInterceptor).addPathPatterns("/**")
    }
}