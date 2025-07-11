package com.goodpon.infra.db.jpa.adapter.partner

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.infra.db.jpa.core.CouponTemplateCoreRepository
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import org.springframework.stereotype.Repository

@Repository("partnerCouponTemplateJpaAdapter")
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun findById(id: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(id)
    }
}
