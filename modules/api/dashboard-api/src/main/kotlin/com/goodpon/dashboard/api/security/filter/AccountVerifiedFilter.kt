package com.goodpon.dashboard.api.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

class AccountVerifiedFilter(
    private val allowListPatterns: List<String> = emptyList(),
) : OncePerRequestFilter() {

    private val pathMatcher = AntPathMatcher()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val requestUri = request.requestURI
        return allowListPatterns.any { pattern -> pathMatcher.match(pattern, requestUri) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication !is com.goodpon.dashboard.api.security.filter.AuthenticationToken) {
            throw BadCredentialsException("유효하지 않은 인증입니다.")
        }

        val isNotVerified = !authentication.principal.verified
        if (isNotVerified) {
            throw AccessDeniedException("계정이 인증되지 않았습니다.")
        }

        filterChain.doFilter(request, response)
    }
}