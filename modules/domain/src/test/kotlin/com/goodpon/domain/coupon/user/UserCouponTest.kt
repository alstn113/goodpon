package com.goodpon.domain.coupon.user

import com.goodpon.domain.coupon.user.exception.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class UserCouponTest : DescribeSpec({

    describe("redeem") {
        it("이미 사용된 쿠폰은 다시 사용할 수 없다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)
            val redeemAt = LocalDateTime.now().plusHours(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )
            val redeemedCoupon = userCoupon.redeem(LocalDateTime.now())

            shouldThrow<UserCouponAlreadyRedeemedException> {
                redeemedCoupon.redeem(redeemAt)
            }
        }

        it("쿠폰이 발급된 상태가 아니면 사용할 수 없다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)
            val redeemAt = LocalDateTime.now().plusHours(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )
            val expiredCoupon = userCoupon.expire()

            shouldThrow<UserCouponRedeemNotAllowedException> {
                expiredCoupon.redeem(redeemAt)
            }
        }

        it("쿠폰 사용 시점이 만료 일시와 같거나 이후면 사용할 수 없다.") {
            val issueAt = LocalDateTime.now()
            forAll(
                row(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1)),
                row(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
            ) { expiresAt: LocalDateTime, redeemAt: LocalDateTime ->

                val userCoupon = UserCoupon.issue(
                    userId = "test-user",
                    couponTemplateId = 1L,
                    issueAt = issueAt,
                    expiresAt = expiresAt
                )

                shouldThrow<UserCouponExpiredException> {
                    userCoupon.redeem(redeemAt)
                }
            }
        }

        it("쿠폰을 사용할 수 있다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)
            val redeemAt = LocalDateTime.now().plusHours(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )

            val redeemedUserCoupon = userCoupon.redeem(redeemAt)

            redeemedUserCoupon.status shouldBe UserCouponStatus.REDEEMED
            redeemedUserCoupon.redeemedAt shouldBe redeemAt
        }
    }

    describe("hasExpired") {
        it("쿠폰이 만료되었는지 확인한다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )

            userCoupon.hasExpired(LocalDateTime.now().plusDays(2)) shouldBe true
            userCoupon.hasExpired(LocalDateTime.now()) shouldBe false
        }
    }

    describe("isOwnedBy") {
        it("쿠폰이 특정 사용자에게 속하는지 확인한다.") {
            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = LocalDateTime.now(),
                expiresAt = LocalDateTime.now().plusDays(1)
            )

            userCoupon.isOwnedBy("test-user") shouldBe true
            userCoupon.isOwnedBy("other-user") shouldBe false
        }
    }

    describe("cancelRedemption") {
        it("쿠폰이 사용된 상태가 아니면 취소할 수 없다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )

            shouldThrow<UserCouponCancelNotAllowedException> {
                userCoupon.cancelRedemption()
            }
        }

        it("쿠폰을 취소할 수 있다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)
            val redeemAt = LocalDateTime.now().plusHours(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )
            val redeemedCoupon = userCoupon.redeem(redeemAt)
            val canceledCoupon = redeemedCoupon.cancelRedemption()

            canceledCoupon.status shouldBe UserCouponStatus.ISSUED
            canceledCoupon.redeemedAt shouldBe null
        }
    }

    describe("expire") {
        it("쿠폰이 발급된 상태가 아니면 만료할 수 없다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)
            val redeemAt = LocalDateTime.now().plusHours(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )
            val redeemedCoupon = userCoupon.redeem(redeemAt)

            shouldThrow<UserCouponExpireNotAllowedException> {
                redeemedCoupon.expire()
            }
        }

        it("쿠폰을 만료할 수 있다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )

            val expiredUserCoupon = userCoupon.expire()

            expiredUserCoupon.status shouldBe UserCouponStatus.EXPIRED
        }
    }

    describe("UserCoupon.issue") {
        it("쿠폰을 발급할 수 있다.") {
            val issueAt = LocalDateTime.now()
            val expiresAt = LocalDateTime.now().plusDays(1)

            val userCoupon = UserCoupon.issue(
                userId = "test-user",
                couponTemplateId = 1L,
                issueAt = issueAt,
                expiresAt = expiresAt
            )

            userCoupon.userId shouldBe "test-user"
            userCoupon.couponTemplateId shouldBe 1L
            userCoupon.status shouldBe UserCouponStatus.ISSUED
            userCoupon.issuedAt shouldBe issueAt
            userCoupon.expiresAt shouldBe expiresAt
        }
    }
})