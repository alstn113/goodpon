package com.goodpon.core.domain.coupon.template.vo

import com.goodpon.core.support.error.CoreException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class CouponPeriodTest : DescribeSpec({

    describe("CouponPeriod 생성") {
        it("발급 종료 시간은 발급 시작 시간과 같거나 이전일 수 없다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()

            forAll(
                row(LocalDate.of(2025, 7, 3).atStartOfDay()),
                row(LocalDate.of(2025, 7, 2).atStartOfDay()),
            ) { issueEndAt: LocalDateTime ->
                val exception = shouldThrow<CoreException> {
                    CouponPeriod(
                        issueStartAt = issueStartAt,
                        issueEndAt = issueEndAt
                    )
                }
                exception.errorType.message shouldBe "발급 종료 시간은 발급 시작 시간 이후여야 합니다."
                exception.errorType.statusCode shouldBe 400
            }
        }

        it("쿠폰 사용 절대 만료 시간은 발급 시작 시간과 같거나 이전일 수 없다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()

            forAll(
                row(LocalDate.of(2025, 7, 3).atStartOfDay()),
                row(LocalDate.of(2025, 7, 2).atStartOfDay()),
            ) { absoluteExpiresAt: LocalDateTime ->
                val exception = shouldThrow<CoreException> {
                    CouponPeriod(
                        issueStartAt = issueStartAt,
                        absoluteExpiresAt = absoluteExpiresAt
                    )
                }
                exception.errorType.message shouldBe "쿠폰 사용 절대 만료 시간은 발급 시작 시간 이후여야 합니다."
                exception.errorType.statusCode shouldBe 400
            }
        }

        it("쿠폰 사용 절대 만료 시간은 발급 종료 시간 이전일 수 없다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
            val issueEndAt = LocalDate.of(2025, 7, 5).atStartOfDay()

            val absoluteExpiresAt = LocalDate.of(2025, 7, 4).atStartOfDay()

            val exception = shouldThrow<CoreException> {
                CouponPeriod(
                    issueStartAt = issueStartAt,
                    issueEndAt = issueEndAt,
                    absoluteExpiresAt = absoluteExpiresAt
                )
            }
            exception.errorType.message shouldBe "쿠폰 사용 절대 만료 시간은 발급 종료 시간 이후이거나 같아야 합니다."
            exception.errorType.statusCode shouldBe 400
        }

        it("쿠폰 사용 절대 만료 시간은 발급 종료 시간과 함께 설정되어야 한다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
            val absoluteExpiresAt = LocalDate.of(2025, 7, 10).atStartOfDay()

            val exception = shouldThrow<CoreException> {
                CouponPeriod(
                    issueStartAt = issueStartAt,
                    absoluteExpiresAt = absoluteExpiresAt
                )
            }

            exception.errorType.message shouldBe "쿠폰 사용 절대 만료 시간은 발급 종료 시간이 설정된 경우에만 설정할 수 있습니다."
            exception.errorType.statusCode shouldBe 400
        }

        it("쿠폰 사용 유효 기간을 설정할 경우 0보다 커야 한다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()

            forAll(
                row(0),
                row(-1)
            ) { validityDays: Int ->
                val exception = shouldThrow<CoreException> {
                    CouponPeriod(
                        issueStartAt = issueStartAt,
                        validityDays = validityDays
                    )
                }
                exception.errorType.message shouldBe "쿠폰 사용 유효 기간을 설정할 경우 0보다 커야 합니다."
                exception.errorType.statusCode shouldBe 400
            }
        }
    }

    describe("isIssuable") {
        it("발급 시간이 발급 시작 시간과 같거나 이후이면 발급할 수 있다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
            val couponPeriod = CouponPeriod(
                issueStartAt = issueStartAt,
                issueEndAt = LocalDate.of(2025, 7, 5).atStartOfDay()
            )

            forAll(
                row(LocalDateTime.of(2025, 7, 3, 0, 0)),
                row(LocalDateTime.of(2025, 7, 4, 0, 0)),
            ) { issueAt: LocalDateTime ->
                couponPeriod.isIssuable(issueAt) shouldBe true
            }
        }

        it("발급 시간이 발급 시작 시간 이전이면 발급할 수 없다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
            val couponPeriod = CouponPeriod(
                issueStartAt = issueStartAt,
                issueEndAt = LocalDateTime.of(2025, 7, 5, 0, 0)
            )
            val issueAt = issueStartAt.minusSeconds(1)

            couponPeriod.isIssuable(issueAt) shouldBe false
        }

        it("발급 시간이 발급 종료 시간과 같거나 이후이면 발급할 수 없다.") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
            val issueEndAt = LocalDate.of(2025, 7, 5).atStartOfDay()
            val couponPeriod = CouponPeriod(
                issueStartAt = issueStartAt,
                issueEndAt = issueEndAt
            )

            forAll(
                row(issueEndAt),
                row(issueEndAt.plusDays(1))
            ) { issueAt: LocalDateTime ->
                couponPeriod.isIssuable(issueAt) shouldBe false
            }
        }
    }

    describe("calculateExpiresAt") {
        context("쿠폰 사용 유효 기간만 있는 경우") {
            it("쿠폰 사용 만료 시간은 발급한 날에 유효 기간을 더한 날의 다음 날 자정(00:00)이다.") {
                val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
                val couponPeriod = CouponPeriod(
                    issueStartAt = issueStartAt,
                    validityDays = 10
                )

                val issueAt = LocalDate.of(2025, 7, 4)
                couponPeriod.calculateExpiresAt(issueAt) shouldBe LocalDate.of(2025, 7, 15).atStartOfDay()
            }
        }

        context("쿠폰 사용 절대 만료 시간만 있는 경우") {
            it("쿠폰 사용 만료 시간은 쿠폰 사용 절대 만료 시간이다.") {
                val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
                val issueEndAt = LocalDate.of(2025, 7, 5).atStartOfDay()
                val absoluteExpiresAt = LocalDate.of(2025, 7, 10).atStartOfDay()
                val couponPeriod = CouponPeriod(
                    issueStartAt = issueStartAt,
                    issueEndAt = issueEndAt,
                    absoluteExpiresAt = absoluteExpiresAt
                )

                val issueAt = LocalDate.of(2025, 7, 4)
                couponPeriod.calculateExpiresAt(issueAt) shouldBe absoluteExpiresAt
            }
        }

        context("쿠폰 사용 유효 기간과 쿠폰 사용 절대 만료일이 모두 있는 경우") {
            val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
            val issueEndAt = LocalDate.of(2025, 7, 5).atStartOfDay()
            val validityDays = 5
            val absoluteExpiresAt = LocalDate.of(2025, 7, 15).atStartOfDay()

            it("쿠폰 사용 절대 만료 시간이 발급 날에 유효 기간을 더한 날의 다음 날 자정(00:00)보다 빠르면 쿠폰 사용 만료 시간은 쿠폰 사용 절대 만료 시간이다.") {
                val couponPeriod = CouponPeriod(
                    issueStartAt = issueStartAt,
                    issueEndAt = issueEndAt,
                    validityDays = validityDays,
                    absoluteExpiresAt = absoluteExpiresAt
                )

                val issueAt = LocalDate.of(2025, 7, 14)
                couponPeriod.calculateExpiresAt(issueAt) shouldBe absoluteExpiresAt
            }

            it("쿠폰 사용 절대 만료 시간이 발급 날에 유효 기간을 더한 날의 다음 날 자정(00:00)보다 늦으면 쿠폰 사용 만료 시간은 발급 날에 유효 기간을 더한 날의 다음 날 자정(00:00)이다.") {
                val couponPeriod = CouponPeriod(
                    issueStartAt = issueStartAt,
                    issueEndAt = issueEndAt,
                    validityDays = validityDays,
                    absoluteExpiresAt = absoluteExpiresAt
                )

                val issueAt = LocalDate.of(2025, 7, 5)
                couponPeriod.calculateExpiresAt(issueAt) shouldBe LocalDate.of(2025, 7, 11).atStartOfDay()
            }
        }

        context("쿠폰 사용 유효 기간과 쿠폰 사용 절대 만료일이 모두 없는 경우") {
            it("쿠폰 사용 만료일은 없다.") {
                val issueStartAt = LocalDate.of(2025, 7, 3).atStartOfDay()
                val couponPeriod = CouponPeriod(
                    issueStartAt = issueStartAt
                )

                val issueAt = LocalDate.of(2025, 7, 4)
                couponPeriod.calculateExpiresAt(issueAt) shouldBe null
            }
        }
    }
})