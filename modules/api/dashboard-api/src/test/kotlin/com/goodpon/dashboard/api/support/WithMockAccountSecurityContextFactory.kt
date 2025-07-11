package com.goodpon.dashboard.api.support

import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.api.security.filter.AuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockAccountSecurityContextFactory : WithSecurityContextFactory<WithMockAccount> {
    override fun createSecurityContext(annotation: WithMockAccount): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val principal = AccountPrincipal(
            id = annotation.id,
            email = annotation.email,
            verified = annotation.verified,
        )
        val authentication = AuthenticationToken.of(
            id = principal.id,
            email = principal.email,
            verified = principal.verified,
        )

        context.authentication = authentication
        return context
    }
}