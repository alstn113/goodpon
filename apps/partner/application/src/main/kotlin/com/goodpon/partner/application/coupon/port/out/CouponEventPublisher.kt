package com.goodpon.partner.application.coupon.port.out

import com.goodpon.partner.application.coupon.port.out.dto.IssueCouponRequestedEvent

interface CouponEventPublisher {

    fun publishIssueCouponRequested(event: IssueCouponRequestedEvent)
}