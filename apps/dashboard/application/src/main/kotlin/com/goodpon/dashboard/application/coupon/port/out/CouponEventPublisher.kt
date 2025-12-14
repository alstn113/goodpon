package com.goodpon.dashboard.application.coupon.port.out

import com.goodpon.dashboard.application.coupon.port.out.dto.IssueCouponRequestedEvent

interface CouponEventPublisher {

    fun publishIssueCouponRequested(event: IssueCouponRequestedEvent)
}