package com.goodpon.infra.security.filter

import com.goodpon.common.domain.account.AccountReader
import com.goodpon.infra.security.exception.BlankTokenException
import com.goodpon.infra.security.exception.InvalidTokenException
import com.goodpon.infra.security.exception.TokenExpiredException
import com.goodpon.infra.security.provider.JwtTokenProvider
import com.goodpon.infra.security.util.AuthHeaderUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val accountReader: AccountReader,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = AuthHeaderUtil.extractBearerToken(request)
        token?.let {

        }
    }

    private fun handleToken(token: String) {
        val accountId = extractAccountId(token)
        val account = accountReader.readById(accountId)

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
}