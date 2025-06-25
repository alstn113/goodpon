package com.goodpon.infra.security.jwt

import com.goodpon.core.application.account.AccountService
import com.goodpon.core.application.account.response.AccountInfo
import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType
import com.goodpon.infra.security.jwt.exception.BlankTokenException
import com.goodpon.infra.security.jwt.exception.InvalidTokenException
import com.goodpon.infra.security.jwt.exception.TokenExpiredException
import com.goodpon.infra.security.util.AuthHeaderUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val accountService: AccountService,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val token = AuthHeaderUtil.extractBearerToken(request)
            token?.let { handleToken(it) }

            filterChain.doFilter(request, response)
        } catch (e: AuthenticationException) {
            SecurityContextHolder.clearContext()
            authenticationEntryPoint.commence(request, response, e)
        }
    }

    private fun handleToken(token: String) {
        val accountId = extractAccountId(token)
        val accountInfo = fetchAccountInfo(accountId)

        val authentication = JwtAuthenticationToken.of(accountInfo.accountId)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun extractAccountId(token: String): Long {
        try {
            return jwtTokenProvider.getAccountId(token)
        } catch (e: BlankTokenException) {
            throw BadCredentialsException("토큰이 비어있습니다.", e)
        } catch (e: TokenExpiredException) {
            throw CredentialsExpiredException("토큰이 만료되었습니다.", e)
        } catch (e: InvalidTokenException) {
            throw BadCredentialsException("유효하지 않은 토큰입니다.", e)
        }
    }

    private fun fetchAccountInfo(accountId: Long): AccountInfo {
        try {
            return accountService.getAccountInfo(accountId)
        } catch (e: CoreException) {
            when (e.errorType) {
                ErrorType.ACCOUNT_NOT_FOUND -> throw BadCredentialsException(ErrorType.ACCOUNT_NOT_FOUND.message, e)
                ErrorType.ACCOUNT_PENDING -> throw BadCredentialsException(ErrorType.ACCOUNT_PENDING.message, e)
                else -> throw BadCredentialsException("계정을 조회하는 중 오류가 발생했습니다.", e)
            }
        } catch (e: Exception) {
            throw BadCredentialsException("계정을 조회하는 중 오류가 발생했습니다.", e)
        }
    }
}