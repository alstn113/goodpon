package com.goodpon.partner.openapi.application

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.application.coupon.service.RedeemCouponService
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import com.goodpon.partner.openapi.support.accessor.TestCouponHistoryAccessor
import com.goodpon.partner.openapi.support.accessor.TestCouponTemplateAccessor
import com.goodpon.partner.openapi.support.accessor.TestCouponTemplateStatsAccessor
import com.goodpon.partner.openapi.support.accessor.TestUserCouponAccessor
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RedeemCouponServiceIT(
    private val redeemCouponService: RedeemCouponService,
    private val issueCouponService: IssueCouponService,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 사용할 수 있다`() {
        // given
        val discountAmount = 2000
        val (merchantId: Long, couponTemplateId: Long) = testCouponTemplateAccessor.save(
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = discountAmount
        )

        val userId = "unique-user-id"
        val issueCouponCommand = IssueCouponCommand(
            userId = userId,
            couponTemplateId = couponTemplateId,
            merchantId = merchantId
        )
        val issueCouponResult = issueCouponService.issueCoupon(issueCouponCommand)

        // when
        val orderId = "unique-order-id"
        val orderAmount = 15000
        val redeemCouponCommand = RedeemCouponCommand(
            merchantId = merchantId,
            couponId = issueCouponResult.userCouponId,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
        val redeemCouponResult = redeemCouponService.redeemCoupon(redeemCouponCommand)

        // then
        redeemCouponResult.couponId shouldBe issueCouponResult.userCouponId
        redeemCouponResult.discountAmount shouldBe discountAmount
        redeemCouponResult.originalPrice shouldBe orderAmount
        redeemCouponResult.finalPrice shouldBe (orderAmount - discountAmount)
        redeemCouponResult.orderId shouldBe orderId

        val foundUserCouponEntity = testUserCouponAccessor.findById(issueCouponResult.userCouponId)
        foundUserCouponEntity.shouldNotBeNull()
        foundUserCouponEntity.status shouldBe UserCouponStatus.REDEEMED

        val foundCouponHistoryEntities = testCouponHistoryAccessor.findByUserCouponId(issueCouponResult.userCouponId)
        foundCouponHistoryEntities.size shouldBe 2
        foundCouponHistoryEntities[0].userCouponId shouldBe issueCouponResult.userCouponId
        foundCouponHistoryEntities[0].actionType.name shouldBe "ISSUE"
        foundCouponHistoryEntities[1].userCouponId shouldBe issueCouponResult.userCouponId
        foundCouponHistoryEntities[1].actionType.name shouldBe "REDEEM"

        val foundCouponTemplateStatsEntity = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundCouponTemplateStatsEntity.shouldNotBeNull()
        foundCouponTemplateStatsEntity.redeemCount shouldBe 1
        foundCouponTemplateStatsEntity.issueCount shouldBe 1
    }
}
