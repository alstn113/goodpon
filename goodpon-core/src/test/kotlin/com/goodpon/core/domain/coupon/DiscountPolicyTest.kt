package com.goodpon.core.domain.coupon

import com.goodpon.core.domain.coupon.vo.DiscountPolicy
import com.goodpon.core.domain.coupon.vo.DiscountType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DiscountPolicyTest : DescribeSpec({

    describe("calculateDiscountAmount") {
        context("할인 정책이 고정 금액 할인(정액)인 경우") {
            it("고정 금액만큼 할인된 금액을 반환한다") {
                val discountPolicy = DiscountPolicy(
                    discountType = DiscountType.FIXED_AMOUNT,
                    discountValue = 2000
                )

                val discountAmount = discountPolicy.calculateDiscountAmount(10000)

                discountAmount shouldBe 2000
            }
        }

        context("할인 정책이 비율 할인(정률)인 경우") {
            it("주문 금액에 비율을 적용하여 할인된 금액을 반환한다") {
                val discountPolicy = DiscountPolicy(
                    discountType = DiscountType.PERCENTAGE,
                    discountValue = 20
                )

                val discountAmount = discountPolicy.calculateDiscountAmount(10000)

                discountAmount shouldBe 2000
            }
        }

        it("할인 금액이 주문 금액보다 큰 경우, 주문 금액만큼 할인된 금액을 반환한다") {
            val discountPolicy = DiscountPolicy(
                discountType = DiscountType.FIXED_AMOUNT,
                discountValue = 15000
            )

            val discountAmount = discountPolicy.calculateDiscountAmount(10000)

            discountAmount shouldBe 10000
        }
    }

    describe("calculateFinalPrice") {
        it("주문 금액에서 할인 금액을 빼서 최종 가격을 계산한다") {
            val discountPolicy = DiscountPolicy(
                discountType = DiscountType.FIXED_AMOUNT,
                discountValue = 2000
            )

            val finalPrice = discountPolicy.calculateFinalPrice(10000)

            finalPrice shouldBe 8000
        }
    }
})