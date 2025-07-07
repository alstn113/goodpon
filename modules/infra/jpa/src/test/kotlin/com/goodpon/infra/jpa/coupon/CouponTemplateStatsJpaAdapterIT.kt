package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.coupon.adapter.CouponTemplateStatsJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.CouponTemplateStatsJpaRepository
import com.goodpon.infra.jpa.support.AbstractJpaIntegrationTest

class CouponTemplateStatsJpaAdapterIT(
    private val couponTemplateStatsJpaAdapter: CouponTemplateStatsJpaAdapter,
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
) : AbstractJpaIntegrationTest()