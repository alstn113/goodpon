package com.goodpon.core.domain.coupon

interface CouponTemplateRepository {

    fun save(couponTemplate: CouponTemplate): CouponTemplate
    fun findById(id: Long): CouponTemplate?
    fun findByIdForRead(id: Long): CouponTemplate?
}