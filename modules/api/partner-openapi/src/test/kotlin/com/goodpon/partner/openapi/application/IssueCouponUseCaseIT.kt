package com.goodpon.partner.openapi.application

import com.goodpon.domain.account.Account
import com.goodpon.partner.application.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class IssueCouponUseCaseIT(
    private val issueCouponUseCase: IssueCouponUseCase,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 발급할 수 있다`() {
        // given
        val account = Account.create(
            email = "test@goodpon.site",
            password = "password",
            name = "테스트 계정",
        )
        val verifiedAccount = account.verify(LocalDateTime.now())
        val savedAccount = accountrepo

    }
}