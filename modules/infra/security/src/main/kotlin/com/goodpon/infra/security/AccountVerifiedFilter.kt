package com.goodpon.infra.security

import com.goodpon.infra.security.jwt.JwtAuthenticationToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class AccountVerifiedFilter(
    private val allowListPaths: List<String> = emptyList(),
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return allowListPaths.any { request.requestURI.startsWith(it) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication !is JwtAuthenticationToken) {
            throw BadCredentialsException("유효하지 않은 인증입니다.")
        }

        val isVerified = !authentication.principal.verified
        if (isVerified) {
            throw AccessDeniedException("계정이 인증되지 않았습니다.")
        }

        filterChain.doFilter(request, response)
    }
}