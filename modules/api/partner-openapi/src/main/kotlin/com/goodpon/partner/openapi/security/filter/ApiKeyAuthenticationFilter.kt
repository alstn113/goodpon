package com.goodpon.partner.openapi.security.filter

import com.goodpon.partner.application.merchant.port.`in`.AuthenticateMerchantUseCase
import com.goodpon.partner.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.partner.application.merchant.service.exception.MerchantClientSecretMismatchException
import com.goodpon.partner.openapi.security.AuthHeaderUtil
import com.goodpon.partner.openapi.security.exception.ClientIdMissingException
import com.goodpon.partner.openapi.security.exception.ClientSecretMissingException
import com.goodpon.partner.openapi.security.exception.InvalidCredentialsException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.filter.OncePerRequestFilter

class ApiKeyAuthenticationFilter(
    private val authenticateMerchantUseCase: AuthenticateMerchantUseCase,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val clientId = AuthHeaderUtil.extractClientId(request)
            if (clientId.isNullOrBlank()) {
                throw ClientIdMissingException()
            }

            val clientSecret = AuthHeaderUtil.extractClientSecret(request)
            if (clientSecret.isNullOrBlank()) {
                throw ClientSecretMissingException()
            }

            authenticateMerchant(clientId = clientId, clientSecret = clientSecret)

            filterChain.doFilter(request, response)
        } catch (e: AuthenticationException) {
            SecurityContextHolder.clearContext()
            authenticationEntryPoint.commence(request, response, e)
        }
    }

    private fun authenticateMerchant(clientId: String, clientSecret: String) {
        try {
            val merchantInfo = authenticateMerchantUseCase(clientId, clientSecret)
            val authentication = ApiKeyAuthenticationToken.of(merchantInfo.id)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: MerchantNotFoundException) {
            throw InvalidCredentialsException("상점을 찾을 수 없습니다. 올바른 클라이언트 ID를 사용했는지 확인해주세요.")
        } catch (e: MerchantClientSecretMismatchException) {
            throw InvalidCredentialsException("Client Secret이 일치하지 않습니다. 올바른 Client Secret을 사용했는지 확인해주세요.")
        }
    }
}