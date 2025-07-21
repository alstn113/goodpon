package com.goodpon.application.dashboard.coupon

import com.goodpon.application.dashboard.coupon.service.ExpireCouponAndTemplateService
import com.goodpon.application.dashboard.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.dashboard.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.dashboard.coupon.service.accessor.UserCouponAccessor
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class ExpireCouponAndTemplateServiceTest : DescribeSpec({

    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val expireCouponAndTemplateService =
        ExpireCouponAndTemplateService(userCouponAccessor, couponHistoryAccessor, couponTemplateAccessor)


    describe("expireExpiredCouponsAndTemplates") {
        it("발급된 쿠폰과 발급 가능한 템플릿을 만료시킨다") {
            // given
            val now = LocalDateTime.now()
            val expireCutoff = now.toLocalDate().atStartOfDay()

            val issuedCoupon = UserCoupon.issue(
                userId = "user123",
                couponTemplateId = 1L,
                expiresAt = expireCutoff.minusDays(1),
                issueAt = expireCutoff.minusDays(2),
            )
            val expiredCoupon = issuedCoupon.expire()

            every {
                userCouponAccessor.readByStatusAndExpiresAtLessThanEqual(UserCouponStatus.ISSUED, expireCutoff)
            } returns listOf(issuedCoupon)
            every {
                couponHistoryAccessor.recordExpired(
                    userCouponId = issuedCoupon.id,
                    recordedAt = now
                )
            } returns mockk()
            every { userCouponAccessor.saveAll(listOf(expiredCoupon)) } returns listOf(expiredCoupon)

            val couponTemplate = mockk<CouponTemplate>()
            every { couponTemplate.expire() } returns couponTemplate
            every {
                couponTemplateAccessor
                    .readByStatusAndAbsoluteExpiresAtLessThanEqual(CouponTemplateStatus.ISSUABLE, expireCutoff)
            } returns listOf(couponTemplate)
            every { couponTemplateAccessor.saveAll(listOf(couponTemplate)) } returns listOf(couponTemplate)

            // when
            expireCouponAndTemplateService(now)

            // then
            verify { couponHistoryAccessor.recordExpired(any(), recordedAt = now) }
            verify { userCouponAccessor.saveAll(listOf(expiredCoupon)) }
            verify {
                couponTemplateAccessor
                    .readByStatusAndAbsoluteExpiresAtLessThanEqual(CouponTemplateStatus.ISSUABLE, expireCutoff)
            }
            verify { couponTemplateAccessor.saveAll(listOf(couponTemplate)) }
        }
    }
})

