package com.goodpon.application.dashboard.coupon.port.out

import com.goodpon.application.dashboard.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.application.dashboard.coupon.service.dto.GetCouponHistoriesQuery
import com.goodpon.domain.coupon.history.CouponHistory

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory

    fun findCouponHistories(query: GetCouponHistoriesQuery): CouponHistoryQueryResult
}