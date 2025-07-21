package com.goodpon.api.dashboard.support

import com.goodpon.api.dashboard.security.filter.AuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockAccountSecurityContextFactory : WithSecurityContextFactory<WithMockAccount> {
    override fun createSecurityContext(annotation: WithMockAccount): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val authentication = AuthenticationToken.of(
            id = annotation.id,
            email = annotation.email,
            verified = annotation.verified,
        )

        context.authentication = authentication
        return context
    }
}