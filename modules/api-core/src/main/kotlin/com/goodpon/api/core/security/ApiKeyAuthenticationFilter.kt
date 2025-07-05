package com.goodpon.api.core.security

import com.goodpon.core.application.merchant.MerchantService
import com.goodpon.core.application.merchant.response.MerchantInfo
import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType
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
            val apiKey = AuthHeaderUtil.extractBearerToken(request)
            apiKey?.let { handleApiKey(it) }

            filterChain.doFilter(request, response)
        } catch (e: AuthenticationException) {
            SecurityContextHolder.clearContext()
            authenticationEntryPoint.commence(request, response, e)
        }
    }

    private fun handleApiKey(apiKey: String) {
        val merchantInfo = fetchMerchantInfo(apiKey)

        val authentication = ApiKeyAuthenticationToken.of(merchantInfo.id)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun fetchMerchantInfo(apiKey: String): MerchantInfo {
        try {
            return merchantService.getMerchantInfoBySecretKey(apiKey)
        } catch (e: CoreException) {
            when (e.errorType) {
                ErrorType.MERCHANT_NOT_FOUND -> throw BadCredentialsException(ErrorType.MERCHANT_NOT_FOUND.message, e)
                else -> throw BadCredentialsException("가맹점을 조회하던 중 오류가 발생했습니다.", e)
            }
        } catch (e: Exception) {
            throw BadCredentialsException("가맹점을 조회하던 중 알 수 없는 오류가 발생했습니다.", e)
        }
    }
}