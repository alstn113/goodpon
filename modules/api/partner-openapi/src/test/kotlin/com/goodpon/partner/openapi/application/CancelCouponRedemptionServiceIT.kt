package com.goodpon.partner.openapi.application

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.service.CancelCouponRedemptionService
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

class CancelCouponRedemptionServiceIT(
    private val cancelCouponRedemptionService: CancelCouponRedemptionService,
    private val issueCouponService: IssueCouponService,
    private val redeemCouponService: RedeemCouponService,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 사용을 취소할 수 있다`() {
        // given

        // - 쿠폰 템플릿 생성
        val (merchantId: Long, couponTemplateId: Long) = testCouponTemplateAccessor.save(
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 2000
        )

        // - 쿠폰 발급
        val userId = "unique-user-id"
        val issueCouponCommand = IssueCouponCommand(
            userId = userId,
            couponTemplateId = couponTemplateId,
            merchantId = merchantId
        )
        val issueCouponResult = issueCouponService(issueCouponCommand)

        // - 쿠폰 사용
        val orderId = "unique-order-id"
        val orderAmount = 15000
        val redeemCouponCommand = RedeemCouponCommand(
            merchantId = merchantId,
            userCouponId = issueCouponResult.userCouponId,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
        val redeemCouponResult = redeemCouponService(redeemCouponCommand)

        // when
        val cancelCouponRedemptionCommand = CancelCouponRedemptionCommand(
            merchantId = merchantId,
            userCouponId = redeemCouponResult.userCouponId,
            orderId = orderId,
            cancelReason = "결제 실패"
        )
        val cancelResult = cancelCouponRedemptionService(cancelCouponRedemptionCommand)

        // then
        cancelResult.userCouponId shouldBe redeemCouponResult.userCouponId
        cancelResult.status shouldBe UserCouponStatus.ISSUED
        cancelResult.canceledAt.shouldNotBeNull()
        cancelResult.cancelReason shouldBe "결제 실패"

        val foundUserCouponEntity = testUserCouponAccessor.findById(redeemCouponResult.userCouponId)
        foundUserCouponEntity.shouldNotBeNull()
        foundUserCouponEntity.status shouldBe UserCouponStatus.ISSUED

        val foundCouponHistoryEntities = testCouponHistoryAccessor.findByUserCouponId(redeemCouponResult.userCouponId)
        foundCouponHistoryEntities.size shouldBe 3
        foundCouponHistoryEntities.sortedBy { it.createdAt }.also { sortedHistory ->
            sortedHistory[0].actionType shouldBe CouponActionType.ISSUE
            sortedHistory[1].actionType shouldBe CouponActionType.REDEEM
            sortedHistory[2].actionType shouldBe CouponActionType.CANCEL_REDEMPTION
        }

        val foundCouponTemplateStatsEntity = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundCouponTemplateStatsEntity.shouldNotBeNull()
        foundCouponTemplateStatsEntity.issueCount shouldBe 1
        foundCouponTemplateStatsEntity.redeemCount shouldBe 0
    }
}