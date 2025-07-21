package com.goodpon.application.dashboard.coupon

import com.goodpon.application.dashboard.coupon.port.out.CouponHistoryRepository
import com.goodpon.application.dashboard.coupon.service.GetCouponHistoriesService
import com.goodpon.application.dashboard.coupon.service.dto.GetCouponHistoriesQuery
import com.goodpon.application.dashboard.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.application.dashboard.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.application.dashboard.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class GetCouponHistoriesServiceTest : DescribeSpec({

    val merchantAccessor = mockk<MerchantAccessor>()
    val couponHistoryRepository = mockk<CouponHistoryRepository>()
    val getCouponHistoriesService = GetCouponHistoriesService(
        merchantAccessor = merchantAccessor,
        couponHistoryRepository = couponHistoryRepository
    )

    val query = GetCouponHistoriesQuery(
        startDate = LocalDate.now(),
        endDate = LocalDate.now(),
        userId = null,
        orderId = null,
        couponTemplateId = null,
        nextCursor = null,
        size = 20
    )

    describe("getCouponHistories") {
        it("상점이 존재하지 않는 경우 예외를 발생시킨다") {
            val merchantId = 1L

            every { merchantAccessor.readById(merchantId) } throws MerchantNotFoundException()

            shouldThrow<MerchantNotFoundException> {
                getCouponHistoriesService(
                    merchantId = merchantId,
                    accountId = 1L,
                    query = query
                )
            }
        }

        it("상점에 접근 권한이 없는 경우 예외를 발생시킨다") {
            val merchantId = 1L
            val accountId = 1L

            val merchant = mockk<Merchant>()
            every { merchantAccessor.readById(merchantId) } returns merchant
            every { merchant.isAccessibleBy(accountId) } returns false

            shouldThrow<NoMerchantAccessPermissionException> {
                getCouponHistoriesService(
                    merchantId = merchantId,
                    accountId = accountId,
                    query = query
                )
            }
        }
    }
})