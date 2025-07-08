package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.IntegrationTest
import com.goodpon.infra.jpa.coupon.adapter.CouponTemplateStatsJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.CouponTemplateStatsJpaRepository

class CouponTemplateStatsJpaAdapterIT(
    private val couponTemplateStatsJpaAdapter: CouponTemplateStatsJpaAdapter,
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
) : IntegrationTest()