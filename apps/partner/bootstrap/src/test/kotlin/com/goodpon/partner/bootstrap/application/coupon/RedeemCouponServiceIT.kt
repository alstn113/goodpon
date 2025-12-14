package com.goodpon.partner.bootstrap.application.coupon

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.port.out.CouponStatsStore
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.application.coupon.service.RedeemCouponService
import com.goodpon.partner.bootstrap.support.AbstractIntegrationTest
import com.goodpon.partner.bootstrap.support.accessor.TestCouponHistoryAccessor
import com.goodpon.partner.bootstrap.support.accessor.TestCouponTemplateAccessor
import com.goodpon.partner.bootstrap.support.accessor.TestMerchantAccessor
import com.goodpon.partner.bootstrap.support.accessor.TestUserCouponAccessor
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RedeemCouponServiceIT(
    private val redeemCouponService: RedeemCouponService,
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val couponStatsStore: CouponStatsStore,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 사용할 수 있다`() {
        // given
        val discountAmount = 2000
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = discountAmount
        )

        val userId = "unique-user-id"
        issueCouponService(
            IssueCouponCommand(
                userId = userId,
                couponTemplateId = couponTemplateId,
                merchantId = merchantId
            )
        )
        val userCoupon = testUserCouponAccessor.issueCouponAndRecord(
            userId = userId,
            couponTemplateId = couponTemplateId,
            merchantId = merchantId
        )

        // when
        val orderId = "unique-order-id"
        val orderAmount = 15000
        val redeemCouponCommand = RedeemCouponCommand(
            merchantId = merchantId,
            userCouponId = userCoupon.id,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
        val redeemCouponResult = redeemCouponService(redeemCouponCommand)

        // then
        redeemCouponResult.userCouponId shouldBe userCoupon.id
        redeemCouponResult.discountAmount shouldBe discountAmount
        redeemCouponResult.originalPrice shouldBe orderAmount
        redeemCouponResult.finalPrice shouldBe (orderAmount - discountAmount)
        redeemCouponResult.orderId shouldBe orderId

        val foundUserCouponEntity = testUserCouponAccessor.findById(userCoupon.id)
        foundUserCouponEntity.shouldNotBeNull()
        foundUserCouponEntity.status shouldBe UserCouponStatus.REDEEMED

        val foundCouponHistoryEntities = testCouponHistoryAccessor.findByUserCouponId(userCoupon.id)
        foundCouponHistoryEntities.size shouldBe 2
        foundCouponHistoryEntities.sortedBy { it.createdAt }.also { sortedHistory ->
            sortedHistory[0].actionType shouldBe CouponActionType.ISSUE
            sortedHistory[1].actionType shouldBe CouponActionType.REDEEM
        }

        couponStatsStore.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 1L
        }
    }
}
