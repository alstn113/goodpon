package com.goodpon.partner.application.coupon

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.out.CouponEventPublisher
import com.goodpon.partner.application.coupon.port.out.CouponIssueStore
import com.goodpon.partner.application.coupon.port.out.dto.IssueCouponRequestedEvent
import com.goodpon.partner.application.coupon.port.out.dto.IssueResult
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class IssueCouponServiceTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val couponIssueStore = mockk<CouponIssueStore>()
    val couponEventPublisher = mockk<CouponEventPublisher>(relaxed = true)

    val issueCouponService = IssueCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        couponIssueStore = couponIssueStore,
        couponEventPublisher = couponEventPublisher
    )

    describe("IssueCouponService") {

        val couponTemplateId = 1L
        val merchantId = 100L
        val userId = "testUser123"
        val command = IssueCouponCommand(
            couponTemplateId = couponTemplateId,
            merchantId = merchantId,
            userId = userId
        )

        val mockTemplate = mockk<CouponTemplate>(relaxed = true) {
            every { id } returns couponTemplateId
            every { maxIssueCount() } returns 10
        }

        context("쿠폰 발급 요청(invoke) 시") {

            context("정상적인 요청이라면") {
                // Given
                every { couponTemplateAccessor.readById(couponTemplateId) } returns mockTemplate
                every { mockTemplate.isOwnedBy(merchantId) } returns true
                every { mockTemplate.validateIssue(any()) } just runs
                every { couponIssueStore.reserveCoupon(any(), any(), any()) } returns IssueResult.SUCCESS

                it("저장소에 예약을 걸고 이벤트를 발행한다") {
                    issueCouponService.invoke(command)

                    // Then
                    verify(exactly = 1) { couponIssueStore.reserveCoupon(couponTemplateId, userId, 10) }
                    verify(exactly = 1) { couponEventPublisher.publishIssueCouponRequested(any<IssueCouponRequestedEvent>()) }
                }
            }

            context("템플릿 조회 실패 및 검증 오류 상황") {

                it("상점이 존재하지 않는 경우 예외를 던진다") {
                    every { couponTemplateAccessor.readById(couponTemplateId) } throws CouponTemplateNotFoundException()

                    shouldThrow<CouponTemplateNotFoundException> {
                        issueCouponService.invoke(command)
                    }
                }

                it("상점이 소유하지 않은 쿠폰 템플릿인 경우 예외를 던진다") {
                    every { couponTemplateAccessor.readById(couponTemplateId) } returns mockTemplate
                    every { mockTemplate.isOwnedBy(merchantId) } returns false

                    shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                        issueCouponService.invoke(command)
                    }
                }

                it("쿠폰 발급 기간이나 상태가 유효하지 않으면 예외를 던진다") {
                    every { couponTemplateAccessor.readById(couponTemplateId) } returns mockTemplate
                    every { mockTemplate.isOwnedBy(merchantId) } returns true
                    every { mockTemplate.validateIssue(any()) } throws IllegalStateException("Expired")

                    shouldThrow<IllegalStateException> {
                        issueCouponService.invoke(command)
                    }
                }
            }

            context("저장소(Redis/DB) 처리 중 오류 발생 시") {
                beforeTest {
                    every { couponTemplateAccessor.readById(couponTemplateId) } returns mockTemplate
                    every { mockTemplate.isOwnedBy(merchantId) } returns true
                    every { mockTemplate.validateIssue(any()) } just runs
                }

                it("이미 발급받은 사용자는 예외를 던지고 이벤트를 발행하지 않는다") {
                    every { couponIssueStore.reserveCoupon(any(), any(), any()) } returns IssueResult.ALREADY_ISSUED

                    shouldThrow<UserCouponAlreadyIssuedException> {
                        issueCouponService.invoke(command)
                    }

                    verify(exactly = 0) {
                        couponEventPublisher.publishIssueCouponRequested(any())
                    }
                }

                it("발급 제한을 초과하면 예외를 던지고 이벤트를 발행하지 않는다") {
                    every {
                        couponIssueStore.reserveCoupon(
                            any(),
                            any(),
                            any()
                        )
                    } returns IssueResult.ISSUE_LIMIT_EXCEEDED

                    shouldThrow<CouponTemplateIssuanceLimitExceededException> {
                        issueCouponService.invoke(command)
                    }

                    verify(exactly = 0) {
                        couponEventPublisher.publishIssueCouponRequested(any())
                    }
                }
            }
        }
    }
})