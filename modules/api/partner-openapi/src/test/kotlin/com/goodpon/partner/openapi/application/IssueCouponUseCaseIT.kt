package com.goodpon.partner.openapi.application

import com.goodpon.partner.application.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import org.junit.jupiter.api.Test

class IssueCouponUseCaseIT(
    private val issueCouponUseCase: IssueCouponUseCase,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 발급할 수 있다`() {
    }
}