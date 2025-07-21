package com.goodpon.api.dashboard.support

import org.springframework.security.test.context.support.WithSecurityContext


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockAccountSecurityContextFactory::class)
annotation class WithMockAccount(
    val id: Long = 1L,
    val email: String = "test@goodpon.site",
    val verified: Boolean = true,
)