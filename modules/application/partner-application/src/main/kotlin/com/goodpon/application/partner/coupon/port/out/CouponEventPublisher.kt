package com.goodpon.application.partner.coupon.port.out

import com.goodpon.application.partner.coupon.port.out.dto.IssueCouponRequestedEvent

interface CouponEventPublisher {

    fun publishIssueCouponRequested(event: IssueCouponRequestedEvent)
}