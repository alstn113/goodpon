package com.goodpon.couponissuer.application.accessor

import com.goodpon.couponissuer.application.port.out.CouponTemplateRepository
import com.goodpon.couponissuer.application.service.accessor.CouponTemplateAccessor
import com.goodpon.couponissuer.application.service.exception.CouponTemplateNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class CouponTemplateAccessorTest : DescribeSpec({

    val couponTemplateRepository = mockk<CouponTemplateRepository>()
    val couponTemplateAccessor = CouponTemplateAccessor(couponTemplateRepository)

    describe("CouponTemplateAccessor") {
        every { couponTemplateRepository.findById(any()) } returns null

        it("쿠폰 템플릿이 존재하지 않으면 예외를 던진다.") {
            shouldThrow<CouponTemplateNotFoundException> {
                couponTemplateAccessor.readById(1L)
            }
        }
    }
})
