package com.goodpon.dashboard.api.security.filter

import com.goodpon.dashboard.api.security.exception.AccountNotVerifiedException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
        if (authentication !is AuthenticationToken) {
            throw BadCredentialsException("유효하지 않은 인증입니다.")
        }

        val isNotVerified = !authentication.principal.verified
        if (isNotVerified) {
            throw AccountNotVerifiedException()
        }

        filterChain.doFilter(request, response)
    }
}