package com.goodpon.partner.openapi.advice

import com.goodpon.partner.openapi.response.ApiResponse
import com.goodpon.partner.openapi.response.TraceIdProvider
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@Order(100)
@RestControllerAdvice
class TraceIdResponseAdvice(
    private val tracerIdProvider: TraceIdProvider,
) : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = returnType.parameterType == ApiResponse::class.java

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        if (body is ApiResponse<*>) {
            val traceId = tracerIdProvider.getTraceId()
            return body.copy(traceId = traceId)
        }
        return body
    }
}
