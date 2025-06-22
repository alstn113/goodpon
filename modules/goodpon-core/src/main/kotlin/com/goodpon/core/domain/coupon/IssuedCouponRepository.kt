package com.goodpon.core.domain.coupon

import java.util.*

interface IssuedCouponRepository {

    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
    fun findByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): IssuedCoupon?
}