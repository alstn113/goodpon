package com.goodpon.core.domain.coupon.template.vo

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class CouponPeriodFactoryTest : DescribeSpec({
    describe("create") {
        it("발급 시작 시간은 발급 시작일 자정(00:00)으로 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val couponPeriod = CouponPeriodFactory.create(issueStartDate = issueStartDate)

            couponPeriod.issueStartAt shouldBe LocalDateTime.of(2025, 7, 3, 0, 0)
        }

        it("발급 종료 시간은 발급 종료일 다음 날 자정(00:00)으로 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 5)
            val couponPeriod = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate
            )

            couponPeriod.issueEndAt shouldBe LocalDateTime.of(2025, 7, 6, 0, 0)
        }

        it("쿠폰 사용 절대 만료 시간은 쿠폰 사용 절대 만료일 다음 날 자정(00:00)으로 한다.") {
            val issueStartDate = LocalDate.of(2025, 7, 3)
            val issueEndDate = LocalDate.of(2025, 7, 5)
            val absoluteExpiryDate = LocalDate.of(2025, 7, 10)
            val couponPeriod = CouponPeriodFactory.create(
                issueStartDate = issueStartDate,
                issueEndDate = issueEndDate,
                absoluteExpiryDate = absoluteExpiryDate
            )

            couponPeriod.absoluteExpiresAt shouldBe LocalDateTime.of(2025, 7, 11, 0, 0)
        }
    }
})