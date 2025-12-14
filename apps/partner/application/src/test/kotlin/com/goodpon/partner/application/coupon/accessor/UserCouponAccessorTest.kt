package com.goodpon.partner.application.coupon.accessor

import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.service.accessor.UserCouponAccessor
import com.goodpon.partner.application.coupon.service.exception.UserCouponNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class UserCouponAccessorTest : DescribeSpec({

    val userCouponRepository = mockk<UserCouponRepository>()
    val userCouponAccessor = UserCouponAccessor(userCouponRepository)

    describe("UserCouponAccessor") {
        every { userCouponRepository.findByIdForUpdate(any()) } returns null

        it("사용자 쿠폰이 존재하지 않는 경우 예외를 던진다") {
            shouldThrow<UserCouponNotFoundException> {
                userCouponAccessor.readByIdForUpdate("non-existent-user-coupon-id")
            }
        }
    }
})