package com.goodpon.partner.openapi.support

import com.goodpon.partner.openapi.security.MerchantPrincipal
import com.goodpon.partner.openapi.security.filter.ApiKeyAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockMerchantSecurityContextFactory : WithSecurityContextFactory<WithMockMerchant> {
    override fun createSecurityContext(annotation: WithMockMerchant): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val principal = MerchantPrincipal(merchantId = annotation.merchantId)
        val authentication = ApiKeyAuthenticationToken.of(merchantId = principal.merchantId)

        context.authentication = authentication
        return context
    }
}