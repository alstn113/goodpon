package com.goodpon.dashboard.application.coupon.port.out

import com.goodpon.dashboard.application.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.dashboard.application.coupon.service.dto.GetCouponHistoriesQuery
import com.goodpon.domain.coupon.history.CouponHistory

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory

    fun findCouponHistories(query: GetCouponHistoriesQuery): CouponHistoryQueryResult
}