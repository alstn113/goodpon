package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.dashboard.application.coupon.service.dto.GetCouponHistoriesQuery

fun interface GetCouponHistoriesUseCase {

    operator fun invoke(merchantId: Long, accountId: Long, query: GetCouponHistoriesQuery): CouponHistoryQueryResult
}