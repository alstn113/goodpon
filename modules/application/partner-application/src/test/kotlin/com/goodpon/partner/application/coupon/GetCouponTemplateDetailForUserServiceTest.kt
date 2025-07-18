package com.goodpon.partner.application.coupon

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.CouponIssuanceStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.GetCouponTemplateDetailForUserQuery
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.GetCouponTemplateDetailForUserService
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class GetCouponTemplateDetailForUserServiceTest : DescribeSpec({

    val couponTemplateRepository = mockk<CouponTemplateRepository>()
    val userCouponRepository = mockk<UserCouponRepository>()


    val getCouponTemplateDetailForUserService = GetCouponTemplateDetailForUserService(
        couponTemplateRepository = couponTemplateRepository,
        userCouponRepository = userCouponRepository,
    )

    describe("GetCouponTemplateDetailForUserService") {
        val query = GetCouponTemplateDetailForUserQuery(
            couponTemplateId = 1L,
            merchantId = 1L,
            userId = "test-user-id"
        )

        it("쿠폰 템플릿이 존재하지 않는 경우 예외를 던진다.") {
            every {
                couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId, query.merchantId)
            } throws CouponTemplateNotFoundException()

            shouldThrow<CouponTemplateNotFoundException> {
                getCouponTemplateDetailForUserService(query)
            }
        }

        context("쿠폰 템플릿이 존재하는 경우") {
            val detail = CouponTemplateDetail(
                id = 1L,
                name = "테스트 쿠폰",
                description = "테스트 쿠폰 설명",
                discountType = CouponDiscountType.FIXED_AMOUNT,
                discountValue = 2000,
                maxDiscountAmount = null,
                minOrderAmount = 15000,
                status = CouponTemplateStatus.ISSUABLE,
                validityDays = null,
                absoluteExpiresAt = null,
                issueStartAt = LocalDateTime.now().minusDays(1),
                issueEndAt = LocalDateTime.now().plusDays(1),
                limitType = CouponLimitPolicyType.ISSUE_COUNT,
                maxIssueCount = 100L,
                maxRedeemCount = null,
                issueCount = 50L,
                redeemCount = 30L,
            )
            beforeTest {
                every {
                    couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId, query.merchantId)
                } returns detail
                every {
                    userCouponRepository.existsByUserIdAndCouponTemplateId(query.userId!!, query.couponTemplateId)
                } returns false
            }

            it("발급 기간 전인 경우 issuanceStatus가 PERIOD_NOT_STARTED로 설정된다.") {
                every {
                    couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId, query.merchantId)
                } returns detail.copy(issueStartAt = LocalDateTime.now().plusDays(1))

                val result = getCouponTemplateDetailForUserService(query)

                result.issuanceStatus shouldBe CouponIssuanceStatus.PERIOD_NOT_STARTED
            }

            it("발급 기간 종료 후인 경우 issuanceStatus가 PERIOD_ENDED로 설정된다.") {
                every {
                    couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId, query.merchantId)
                } returns detail.copy(issueEndAt = LocalDateTime.now().minusDays(1))

                val result = getCouponTemplateDetailForUserService(query)

                result.issuanceStatus shouldBe CouponIssuanceStatus.PERIOD_ENDED
            }

            it("userId가 null이 아니고, 이미 발급된 쿠폰이 있는 경우 issuanceStatus가 ALREADY_ISSUED_BY_USER로 설정된다.") {
                every {
                    userCouponRepository.existsByUserIdAndCouponTemplateId(query.userId!!, query.couponTemplateId)
                } returns true

                val result = getCouponTemplateDetailForUserService(query)

                result.issuanceStatus shouldBe CouponIssuanceStatus.ALREADY_ISSUED_BY_USER
            }

            it("최대 발급 수량을 초과한 경우 issuanceStatus가 MAX_ISSUE_COUNT_EXCEEDED로 설정된다.") {
                every {
                    couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId, query.merchantId)
                } returns detail.copy(
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = 100L,
                    issueCount = 100L,
                    maxRedeemCount = null,
                )

                val result = getCouponTemplateDetailForUserService(query)

                result.issuanceStatus shouldBe CouponIssuanceStatus.MAX_ISSUE_COUNT_EXCEEDED
            }

            it("최대 사용 수량을 초과한 경우 issuanceStatus가 MAX_REDEEM_COUNT_EXCEEDED로 설정된다.") {
                every {
                    couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId, query.merchantId)
                } returns detail.copy(
                    limitType = CouponLimitPolicyType.REDEEM_COUNT,
                    maxRedeemCount = 100L,
                    redeemCount = 100L,
                    maxIssueCount = null
                )

                val result = getCouponTemplateDetailForUserService(query)

                result.issuanceStatus shouldBe CouponIssuanceStatus.MAX_REDEEM_COUNT_EXCEEDED
            }

            it("사용자에 따른 쿠폰 템플릿 상세 정보를 반환한다.") {
                val result = getCouponTemplateDetailForUserService(query)

                result.issuanceStatus shouldBe CouponIssuanceStatus.AVAILABLE
            }
        }
    }
})