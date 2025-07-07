package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.coupon.adapter.CouponTemplateJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.CouponTemplateJpaRepository
import com.goodpon.infra.jpa.IntegrationTest

class CouponTemplateJpaAdapterIT(
    private val couponTemplateJpaAdapter: CouponTemplateJpaAdapter,
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
) : IntegrationTest()