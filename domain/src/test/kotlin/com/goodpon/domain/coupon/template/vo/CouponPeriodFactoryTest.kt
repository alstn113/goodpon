package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.time.LocalDate
import java.time.LocalDateTime

class CouponPeriodFactoryTest : DescribeSpec({

    describe("create") {
        it("발급 시작 일시는 발급 시작일 자정(00:00)으로 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val couponPeriod = CouponPeriodFactory.create(issueStartDate = issueStartDate).getOrThrow()

            couponPeriod.issueStartAt shouldBe LocalDateTime.of(2025, 7, 3, 0, 0)
        }

        it("발급 종료 일시는 발급 종료일 다음 날 자정(00:00)으로 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 5)
            val couponPeriod = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate
            ).getOrThrow()

            couponPeriod.issueEndAt shouldBe LocalDateTime.of(2025, 7, 6, 0, 0)
        }

        it("쿠폰 사용 절대 만료 일시는 쿠폰 사용 절대 만료일 다음 날 자정(00:00)으로 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 5)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 10)
            val couponPeriod = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
                absoluteExpiryDate = absoluteExpiryDate
            ).getOrThrow()

            couponPeriod.absoluteExpiresAt shouldBe LocalDateTime.of(2025, 7, 11, 0, 0)
        }
    }

    describe("create - validate") {
        it("쿠폰 발급 종료일은 발급 시작일 이전일 수 없다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 2)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
            )

            result.exceptionOrNull().shouldBeInstanceOf<CouponPeriodInvalidIssueEndBeforeStartException>()
        }

        it("쿠폰 발급 종료일은 발급 시작일과 같을 수 있다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 3)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate
            )

            result.isSuccess shouldBe true
        }

        it("쿠폰 사용 절대 만료일은 발급 시작일보다 이전일 수 없다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 3)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 2)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
                absoluteExpiryDate = absoluteExpiryDate
            )

            result.exceptionOrNull().shouldBeInstanceOf<CouponPeriodInvalidExpireBeforeStartException>()
        }

        it("쿠폰 사용 절대 만료일은 발급 시작일과 같을 수 있다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 3)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 3)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
                absoluteExpiryDate = absoluteExpiryDate
            )

            result.isSuccess shouldBe true
        }

        it("쿠폰 사용 절대 만료일은 발급 종료일보다 이전일 수 없다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 5)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 4)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
                absoluteExpiryDate = absoluteExpiryDate
            )

            result.exceptionOrNull().shouldBeInstanceOf<CouponPeriodInvalidExpireBeforeIssueEndException>()
        }

        it("쿠폰 사용 절대 만료일은 발급 종료일과 같을 수 있다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 5)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 5)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
                absoluteExpiryDate = absoluteExpiryDate
            )

            result.isSuccess shouldBe true
        }

        it("쿠폰 사용 절대 만료일은 발급 종료일과 함께 설정되어야 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 10)

            val result = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                absoluteExpiryDate = absoluteExpiryDate
            )

            result.exceptionOrNull().shouldBeInstanceOf<CouponPeriodInvalidExpireWithoutIssueEndException>()
        }

        it("쿠폰 사용 유효 기간을 설정할 경우 0보다 커야 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)

            forAll(
                row(0),
                row(-1)
            ) { validityDays: Int ->
                val result = CouponPeriodFactory.create(
                    issueStartDate = issueStartDate,
                    validityDays = validityDays
                )

                result.exceptionOrNull().shouldBeInstanceOf<CouponPeriodInvalidValidityDaysException>()
            }
        }
    }
})