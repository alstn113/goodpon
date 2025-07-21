package com.goodpon.application.dashboard.coupon.port.`in`

import com.goodpon.application.dashboard.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.application.dashboard.coupon.service.dto.GetCouponHistoriesQuery

fun interface GetCouponHistoriesUseCase {

    operator fun invoke(merchantId: Long, accountId: Long, query: GetCouponHistoriesQuery): CouponHistoryQueryResult
}