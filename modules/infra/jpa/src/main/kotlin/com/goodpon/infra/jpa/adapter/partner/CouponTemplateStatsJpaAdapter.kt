package com.goodpon.infra.jpa.adapter.partner

import com.goodpon.infra.jpa.core.CouponTemplateStatsCoreRepository
import com.goodpon.infra.jpa.repository.CouponTemplateStatsJpaRepository
import com.goodpon.partner.application.coupon.port.out.CouponTemplateStatsRepository
import org.springframework.stereotype.Repository

@Repository("partnerCouponTemplateStatsJpaAdapter")
class CouponTemplateStatsJpaAdapter(
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
) : CouponTemplateStatsCoreRepository(couponTemplateStatsJpaRepository), CouponTemplateStatsRepository
