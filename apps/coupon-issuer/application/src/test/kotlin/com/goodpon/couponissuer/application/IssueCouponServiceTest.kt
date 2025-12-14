package com.goodpon.couponissuer.application

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.application.service.IssueCouponService
import com.goodpon.couponissuer.application.service.accessor.CouponHistoryAccessor
import com.goodpon.couponissuer.application.service.accessor.CouponTemplateAccessor
import com.goodpon.couponissuer.application.service.accessor.UserCouponAccessor
import com.goodpon.couponissuer.application.service.listener.CouponIssuedEvent
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime

class IssueCouponServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)

    val issueCouponService = IssueCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        userCouponAccessor = userCouponAccessor,
        couponHistoryAccessor = couponHistoryAccessor,
        eventPublisher = eventPublisher
    )

    afterTest {
        clearAllMocks()
    }

    describe("IssueCouponService") {
        val userId = "user-1"
        val templateId = 100L
        val now = LocalDateTime.now()
        val command = IssueCouponCommand(
            couponTemplateId = templateId,
            userId = userId,
            requestedAt = now
        )

        val mockTemplate = mockk<CouponTemplate>(relaxed = true)
        val mockUserCoupon = mockk<UserCoupon>(relaxed = true) {
            every { id } returns "issued-coupon-uuid"
        }

        beforeTest {
            every { couponTemplateAccessor.readById(templateId) } returns mockTemplate
        }

        context("쿠폰 발급 요청(invoke)이 들어왔을 때") {

            context("해당 유저가 아직 쿠폰을 발급받지 않은 상태라면") {
                every { userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, templateId) } returns false
                every { userCouponAccessor.save(any()) } returns mockUserCoupon
                every { couponHistoryAccessor.recordIssued(any(), any()) } returns mockk<CouponHistory>()

                it("쿠폰을 새로 발급하여 저장하고, 발급 이력을 기록한다") {
                    issueCouponService.invoke(command)

                    verify(exactly = 1) { eventPublisher.publishEvent(any<CouponIssuedEvent>()) }
                    verify(exactly = 1) { userCouponAccessor.save(any()) }
                    verify(exactly = 1) { couponHistoryAccessor.recordIssued("issued-coupon-uuid", now) }
                }
            }

            context("해당 유저가 이미 쿠폰을 발급받은 상태라면") {
                every { userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, templateId) } returns true

                it("이벤트는 발행하지만, 중복 저장을 막기 위해 로직을 종료한다") {
                    issueCouponService.invoke(command)

                    verify(exactly = 1) { eventPublisher.publishEvent(any<CouponIssuedEvent>()) }
                    verify(exactly = 0) { userCouponAccessor.save(any()) }
                    verify(exactly = 0) { couponHistoryAccessor.recordIssued(any(), any()) }
                }
            }
        }
    }
})