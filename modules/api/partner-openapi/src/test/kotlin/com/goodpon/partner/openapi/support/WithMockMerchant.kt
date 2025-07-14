package com.goodpon.partner.openapi.support

import org.springframework.security.test.context.support.WithSecurityContext


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockMerchantSecurityContextFactory::class)
annotation class WithMockMerchant(
    val merchantId: Long = 1L,
)