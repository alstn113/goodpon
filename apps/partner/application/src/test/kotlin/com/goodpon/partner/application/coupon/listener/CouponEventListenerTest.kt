package com.goodpon.partner.application.coupon.listener

import com.goodpon.partner.application.coupon.port.out.CouponIssueStore
import com.goodpon.partner.application.coupon.service.listener.CouponEventListener
import com.goodpon.partner.application.coupon.service.listener.RedeemCouponRollbackEvent
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class CouponEventListenerTest : DescribeSpec({

    val couponIssueStore = mockk<CouponIssueStore>()
    val listener = CouponEventListener(couponIssueStore)

    describe("CouponEventListener") {

        context("쿠폰 사용 롤백 이벤트(RedeemCouponRollbackEvent)가 발생하면") {
            val event = RedeemCouponRollbackEvent(
                couponTemplateId = 1L,
                userId = "user1"
            )

            it("저장소에 사용 취소 요청(cancelRedeem)을 보낸다") {
                // Given
                every { couponIssueStore.cancelRedeem(event.couponTemplateId, event.userId) } just runs

                // When
                listener.handleRedeemCouponRollbackEvent(event)

                // Then
                verify(exactly = 1) {
                    couponIssueStore.cancelRedeem(event.couponTemplateId, event.userId)
                }
            }
        }
    }
})