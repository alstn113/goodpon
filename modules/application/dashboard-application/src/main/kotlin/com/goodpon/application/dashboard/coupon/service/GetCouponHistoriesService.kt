package com.goodpon.application.dashboard.coupon.service

import com.goodpon.application.dashboard.coupon.port.`in`.GetCouponHistoriesUseCase
import com.goodpon.application.dashboard.coupon.port.out.CouponHistoryRepository
import com.goodpon.application.dashboard.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.application.dashboard.coupon.service.dto.GetCouponHistoriesQuery
import com.goodpon.application.dashboard.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.application.dashboard.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCouponHistoriesService(
    private val merchantAccessor: MerchantAccessor,
    private val couponHistoryRepository: CouponHistoryRepository,
) : GetCouponHistoriesUseCase {

    @Transactional(readOnly = true)
    override fun invoke(merchantId: Long, accountId: Long, query: GetCouponHistoriesQuery): CouponHistoryQueryResult {
        val merchant = merchantAccessor.readById(merchantId)

        if (!merchant.isAccessibleBy(accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        return couponHistoryRepository.findCouponHistories(query)
    }
}