package com.goodpon.partner.bootstrap.support

import com.goodpon.partner.application.coupon.port.out.CouponEventPublisher
import com.goodpon.partner.application.coupon.port.out.dto.IssueCouponRequestedEvent
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Primary
@Profile("test")
@Component
class FakeCouponEventPublisher : CouponEventPublisher {

    private var publishedEvent: IssueCouponRequestedEvent? = null

    override fun publishIssueCouponRequested(event: IssueCouponRequestedEvent) {
        this.publishedEvent = event
    }

    fun consume(): IssueCouponRequestedEvent? {
        val event = this.publishedEvent
        this.publishedEvent = null
        return event
    }
}