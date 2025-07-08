package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.IntegrationTest
import com.goodpon.infra.jpa.coupon.adapter.CouponTemplateJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.CouponTemplateJpaRepository

class CouponTemplateJpaAdapterIT(
    private val couponTemplateJpaAdapter: CouponTemplateJpaAdapter,
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
) : IntegrationTest()