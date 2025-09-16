package com.goodpon.api.partner.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper

class RequestBodyCachingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val wrappedRequest = CachedBodyHttpServletRequest(request)
        val wrappedResponse = ContentCachingResponseWrapper(response) // 5xx와 특정 4xx 응답을 제외하기 위해서 body 를 읽음.

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            wrappedResponse.copyBodyToResponse() // wrapper 에는 응답이 기록되지만, 실제 응답에는 기록되지 않으므로 복사해줘야 함
        }
    }
}