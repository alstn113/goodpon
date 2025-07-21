package com.goodpon.api.partner.application.coupon

import com.goodpon.api.partner.support.AbstractIntegrationTest
import com.goodpon.api.partner.support.accessor.*
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
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
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
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

        val foundCouponTemplateStatsEntity = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundCouponTemplateStatsEntity.shouldNotBeNull()
        foundCouponTemplateStatsEntity.issueCount shouldBe 1
        foundCouponTemplateStatsEntity.redeemCount shouldBe 0

        val foundCouponHistoryEntities = testCouponHistoryAccessor.findByUserCouponId(result.userCouponId)
        foundCouponHistoryEntities.shouldNotBeNull()
        foundCouponHistoryEntities.size shouldBe 1
        foundCouponHistoryEntities[0].userCouponId shouldBe result.userCouponId
        foundCouponHistoryEntities[0].actionType shouldBe CouponActionType.ISSUE
    }
}
