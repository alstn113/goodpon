package com.goodpon.api.partner.application.coupon

import com.goodpon.api.partner.support.AbstractIntegrationTest
import com.goodpon.api.partner.support.accessor.TestCouponHistoryAccessor
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.api.partner.support.accessor.TestUserCouponAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.IssueCouponService
import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.user.UserCouponStatus
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IssueCouponServiceIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 발급할 수 있다`() {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "unique-user-id"

        val command = IssueCouponCommand(
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
            userId = userId,
        )

        // when
        val result = issueCouponService(command = command)

        // then
        result.userId shouldBe userId
        result.couponTemplateId shouldBe couponTemplateId

        val foundUserCouponEntity = testUserCouponAccessor.findById(result.userCouponId)
        foundUserCouponEntity.shouldNotBeNull()
        foundUserCouponEntity.userId shouldBe userId
        foundUserCouponEntity.couponTemplateId shouldBe couponTemplateId
        foundUserCouponEntity.status shouldBe UserCouponStatus.ISSUED

        couponTemplateStatsCache.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 0L
        }

        val foundCouponHistoryEntities = testCouponHistoryAccessor.findByUserCouponId(result.userCouponId)
        foundCouponHistoryEntities.shouldNotBeNull()
        foundCouponHistoryEntities.size shouldBe 1
        foundCouponHistoryEntities[0].userCouponId shouldBe result.userCouponId
        foundCouponHistoryEntities[0].actionType shouldBe CouponActionType.ISSUE
    }
}
