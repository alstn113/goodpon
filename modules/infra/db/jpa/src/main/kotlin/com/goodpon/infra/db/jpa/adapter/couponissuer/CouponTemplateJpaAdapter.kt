package com.goodpon.infra.db.jpa.adapter.couponissuer

import com.goodpon.application.couponissuer.port.out.CouponTemplateRepository
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.infra.db.jpa.core.CouponTemplateCoreRepository
import org.springframework.stereotype.Repository

@Repository("couponIssuerCouponTemplateJpaAdapter")
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun findById(couponTemplateId: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(couponTemplateId)
    }
}