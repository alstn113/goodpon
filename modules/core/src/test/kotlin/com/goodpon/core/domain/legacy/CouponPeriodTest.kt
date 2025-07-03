package com.goodpon.core.domain.legacy

import com.goodpon.core.domain.coupon.template.vo.CouponPeriod
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CouponPeriodTest : DescribeSpec({
//
//    val today = LocalDate.of(2025, 6, 13)
//    val tomorrow = today.plusDays(1)
//    val dayAfterTomorrow = today.plusDays(2)
//    val yesterday = today.minusDays(1)
//
//    describe("CouponPeriod.create") {
//
//        it("발급 시작 일시는 자정으로 표현된다") {
//            // when
//            val period = CouponPeriod.create(issueStartDate = today)
//
//            // then
//            period.issueStartAt shouldBe today.atStartOfDay()
//        }
//
//        it("발급 종료 일시와 사용 종료 일시는 다음날 자정으로 표현된다") {
//            // when
//            val period = CouponPeriod.create(
//                issueStartDate = today,
//                issueEndDate = tomorrow,
//                useEndDate = tomorrow,
//            )
//
//            // then
//            period.issueEndAt shouldBe tomorrow.plusDays(1).atStartOfDay()
//            period.absoluteExpiresAt shouldBe tomorrow.plusDays(1).atStartOfDay()
//        }
//
//        it("발급 종료 일자는 발급 시작 일자 이후여야 한다") {
//            // when
//            val exception = shouldThrow<IllegalArgumentException> {
//                CouponPeriod.create(issueStartDate = tomorrow, issueEndDate = today)
//            }
//
//            // then
//            exception.message shouldBe "발급 종료 기간은 발급 시작 기간 이후여야 합니다."
//        }
//
//        it("발급 시작 일자와 발급 종료 일자는 같을 수 있다") {
//            // when
//            val period = CouponPeriod.create(issueStartDate = today, issueEndDate = today)
//
//            // Then
//            period.issueStartAt shouldBe today.atStartOfDay()
//            period.issueEndAt shouldBe today.plusDays(1).atStartOfDay()
//        }
//
//        it("사용 종료 일자는 발급 시작 일자 이후여야 한다") {
//            // when
//            val exception = shouldThrow<IllegalArgumentException> {
//                CouponPeriod.create(issueStartDate = tomorrow, useEndDate = today)
//            }
//
//            // then
//            exception.message shouldBe "사용 종료 기간은 발급 시작 기간 이후여야 합니다."
//        }
//
//        it("사용 종료 일자는 발급 종료 일자 이후이거나 같아야 한다") {
//            // when
//            val exception = shouldThrow<IllegalArgumentException> {
//                CouponPeriod.create(issueStartDate = today, issueEndDate = tomorrow, useEndDate = today)
//            }
//
//            // then
//            exception.message shouldBe "사용 종료 기간은 발급 종료 기간 이후이거나 같아야 합니다."
//        }
//
//        it("발급 종료 기간이 없으면 사용 종료 기간을 설정할 수 없다") {
//            // when
//            val exception = shouldThrow<IllegalArgumentException> {
//                CouponPeriod.create(issueStartDate = today, useEndDate = tomorrow)
//            }
//
//            // then
//            exception.message shouldBe "발급 종료 기간이 없으면 사용 종료 기간을 설정할 수 없습니다."
//        }
//
//        it("유효 기간은 0보다 커야 한다") {
//            // when
//            val exception = shouldThrow<IllegalArgumentException> {
//                CouponPeriod.create(issueStartDate = today, validityDays = 0)
//            }
//
//            // then
//            exception.message shouldBe "유효 기간은 0보다 커야 합니다."
//        }
//    }
//
//    describe("isIssuable") {
//
//        it("현재 시간이 발급 시작 일시 이전이면 발급 불가능하다") {
//            // given
//            val period = CouponPeriod.create(issueStartDate = today)
//
//            // when
//            val result = period.isIssuable(yesterday.atTime(12, 30))
//
//            // then
//            result shouldBe false
//        }
//
//        it("발급 종료 일시가 없을 경우 발급 가능하다") {
//            // given
//            val period = CouponPeriod.create(issueStartDate = today, issueEndDate = null)
//
//            // when
//            val result = period.isIssuable(today.atTime(12, 30))
//
//            // then
//            result shouldBe true
//        }
//
//        it("현재 시간이 발급 종료 일시 이후이면 발급 불가능하다") {
//            // given
//            val period = CouponPeriod.create(issueStartDate = today, issueEndDate = tomorrow)
//
//            // when
//            val result = period.isIssuable(dayAfterTomorrow.atTime(12, 30))
//
//            // then
//            result shouldBe false
//        }
//
//        it("현재 시간이 발급 종료 일시 이전이면 발급 가능하다") {
//            // given
//            val period = CouponPeriod.create(issueStartDate = today, issueEndDate = tomorrow)
//
//            // when
//            val result = period.isIssuable(tomorrow.atTime(12, 30))
//
//            // then
//            result shouldBe true
//        }
//    }
//
//    describe("calculateFinalUseEndAt") {
//
//        it("사용 종료 일시는 발급 일자에 유효 기간을 더한 날의 다음날 자정으로 계산된다") {
//            // given
//            val period = CouponPeriod.create(
//                issueStartDate = today,
//                issueEndDate = tomorrow,
//                validityDays = 3,
//                useEndDate = today.plusDays(7)
//            )
//
//            // when
//            val finalUseEndAt = period.calculateFinalUseEndAt(today)
//
//            // then
//            finalUseEndAt shouldBe today.plusDays(4).atStartOfDay()
//        }
//
//        context("유효 기간과 사용 종료 일시가 모두 설정되어 있지 않은 경우") {
//
//            it("무기한을 의미하는 null을 반환한다.") {
//                // given
//                val period = CouponPeriod.create(issueStartDate = today)
//
//                // when
//                val finalUseEndAt = period.calculateFinalUseEndAt(today)
//
//                // then
//                finalUseEndAt shouldBe null
//            }
//        }
//
//        context("유효 기간과 사용 종료 일시가 모두 설정된 경우") {
//
//            it("사용 종료 일시가 유효 기간에 따라 계산된 날보다 늦거나 같은 경우, 유효 기간에 따라 계산된 날을 반환한다") {
//                // given
//                val period = CouponPeriod.create(
//                    issueStartDate = today,
//                    issueEndDate = tomorrow,
//                    validityDays = 3,
//                    useEndDate = today.plusDays(5)
//                )
//
//                // when
//                val finalUseEndAt = period.calculateFinalUseEndAt(today)
//
//                // then
//                finalUseEndAt shouldBe today.plusDays(4).atStartOfDay()
//            }
//
//            it("사용 종료 일시가 유효 기간에 따라 계산된 날보다 빠른 경우, 사용 종료 일시를 반환한다") {
//                // given
//                val period = CouponPeriod.create(
//                    issueStartDate = today,
//                    issueEndDate = tomorrow,
//                    validityDays = 3,
//                    useEndDate = today.plusDays(2)
//                )
//
//                // when
//                val finalUseEndAt = period.calculateFinalUseEndAt(today)
//
//                // then
//                finalUseEndAt shouldBe today.plusDays(3).atStartOfDay()
//            }
//        }
//
//        it("유효 기간이 설정되지 않은 경우, 사용 종료 일시가 설정되어 있으면 사용 종료 일시를 반환한다") {
//            // given
//            val period = CouponPeriod.create(
//                issueStartDate = today,
//                issueEndDate = tomorrow,
//                useEndDate = today.plusDays(5)
//            )
//
//            // when
//            val finalUseEndAt = period.calculateFinalUseEndAt(today)
//
//            // then
//            finalUseEndAt shouldBe today.plusDays(6).atStartOfDay()
//        }
//    }
})
