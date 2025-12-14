package com.goodpon.couponissuer.application

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.application.port.out.CouponIssueStore
import com.goodpon.couponissuer.application.service.IssueCouponService
import com.goodpon.couponissuer.application.service.accessor.CouponHistoryAccessor
import com.goodpon.couponissuer.application.service.accessor.CouponTemplateAccessor
import com.goodpon.couponissuer.application.service.accessor.UserCouponAccessor
import com.goodpon.couponissuer.application.service.listener.CouponIssuedEvent
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime

class IssueCouponServiceTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    // Mocks
    val couponIssueStore = mockk<CouponIssueStore>()
    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)

    val issueCouponService = IssueCouponService(
        couponIssueStore = couponIssueStore,
        couponTemplateAccessor = couponTemplateAccessor,
        userCouponAccessor = userCouponAccessor,
        couponHistoryAccessor = couponHistoryAccessor,
        eventPublisher = eventPublisher
    )

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

        context("쿠폰 발급 요청(invoke) 시") {

            context("Redis에 발급 예약 정보가 없다면 (유효하지 않은 접근)") {
                // Given
                every { couponIssueStore.existsIssueReservation(templateId, userId) } returns false

                it("아무런 동작도 하지 않고 종료한다 (이벤트X, 저장X)") {
                    // When
                    issueCouponService.invoke(command)

                    // Then
                    verify(exactly = 0) { eventPublisher.publishEvent(any()) }
                    verify(exactly = 0) { userCouponAccessor.save(any()) }
                    verify(exactly = 0) { couponTemplateAccessor.readById(any()) }
                }
            }

            context("발급 예약 정보가 존재하고") {
                // Given (Common)
                every { couponIssueStore.existsIssueReservation(templateId, userId) } returns true

                context("해당 유저가 아직 쿠폰을 발급받지 않은 상태라면") {
                    // Given
                    every { couponTemplateAccessor.readById(templateId) } returns mockTemplate
                    every { userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, templateId) } returns false
                    every { userCouponAccessor.save(any()) } returns mockUserCoupon
                    every { couponHistoryAccessor.recordIssued(any(), any()) } returns mockk<CouponHistory>()

                    it("이벤트를 발행하고, 쿠폰을 새로 저장하며, 이력을 기록한다") {
                        // When
                        issueCouponService.invoke(command)

                        // Then
                        verify(exactly = 1) { eventPublisher.publishEvent(any<CouponIssuedEvent>()) }
                        verify(exactly = 1) { userCouponAccessor.save(any()) }
                        verify(exactly = 1) { couponHistoryAccessor.recordIssued("issued-coupon-uuid", now) }
                    }
                }

                context("해당 유저가 이미 쿠폰을 발급받은 상태라면") {
                    // Given
                    every { userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, templateId) } returns true

                    it("상태 정합성을 위해 이벤트는 발행하지만, 저장은 하지 않고 종료한다") {
                        // When
                        issueCouponService.invoke(command)

                        // Then
                        verify(exactly = 1) { eventPublisher.publishEvent(any<CouponIssuedEvent>()) }
                        verify(exactly = 0) { userCouponAccessor.save(any()) }
                        verify(exactly = 0) { couponHistoryAccessor.recordIssued(any(), any()) }
                    }
                }
            }
        }
    }
})