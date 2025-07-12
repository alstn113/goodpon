package com.goodpon.partner.openapi.security.filter

import com.goodpon.partner.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.partner.application.merchant.service.MerchantService
import com.goodpon.partner.application.merchant.service.exception.MerchantClientSecretMismatchException
import com.goodpon.partner.openapi.security.AuthHeaderUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.filter.OncePerRequestFilter

class ApiKeyAuthenticationFilter(
    private val merchantService: MerchantService,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val clientId = AuthHeaderUtil.extractClientId(request)
            val clientSecret = AuthHeaderUtil.extractClientSecret(request)

            if (clientId.isNullOrBlank() || clientSecret.isNullOrBlank()) {
                throw BadCredentialsException("Client ID 또는 Client Secret이 누락되었습니다.")
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
            val merchantInfo = merchantService.authenticate(clientId = clientId, clientSecret = clientSecret)
            val authentication = ApiKeyAuthenticationToken.of(merchantInfo.id)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: MerchantNotFoundException) {
            throw BadCredentialsException("상점을 조회하던 중 오류가 발생했습니다.", e)
        } catch (e: MerchantClientSecretMismatchException) {
            throw BadCredentialsException("상점의 Client Secret이 일치하지 않습니다.", e)
        } catch (e: Exception) {
            throw BadCredentialsException("상점을 조회하던 중 알 수 없는 오류가 발생했습니다.", e)
        }
    }
}