package com.goodpon.application.dashboard.coupon.port.out

import com.goodpon.application.dashboard.coupon.port.out.dto.IssueCouponRequestedEvent

interface CouponEventPublisher {

    fun publishIssueCouponRequested(event: IssueCouponRequestedEvent)
}