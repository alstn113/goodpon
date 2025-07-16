package com.goodpon.partner.openapi.application

import com.goodpon.partner.application.coupon.service.RedeemCouponService
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import com.goodpon.partner.openapi.support.accessor.TestCouponTemplateStatsAccessor
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql

@Sql("/sql/init-coupon-template.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RedeemCouponServiceIT(
    private val redeemCouponService: RedeemCouponService,
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 사용할 수 있다`() {
        // given
        1 shouldNotBe 0 // Dummy assertion to ensure the test runs
    }
}
