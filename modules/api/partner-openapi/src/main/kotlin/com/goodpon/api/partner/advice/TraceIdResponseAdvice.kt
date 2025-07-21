package com.goodpon.api.partner.advice

import com.goodpon.api.partner.response.ApiResponse
import com.goodpon.api.partner.response.TraceIdProvider
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@Order(100)
@RestControllerAdvice(basePackages = ["com.goodpon.api.partner"])
class TraceIdResponseAdvice(
    private val tracerIdProvider: TraceIdProvider,
) : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = true

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        when (body) {
            is ApiResponse<*> -> {
                val traceId = tracerIdProvider.getTraceId()
                return body.copy(traceId = traceId)
            }

            is ResponseEntity<*> -> {
                val responseBody = body.body
                if (responseBody is ApiResponse<*>) {
                    val traceId = tracerIdProvider.getTraceId()
                    val newBody = responseBody.copy(traceId = traceId)

                    return ResponseEntity.status(body.statusCode)
                        .headers(body.headers)
                        .body(newBody)
                }
            }
        }

        return body
    }
}
