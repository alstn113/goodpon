package com.goodpon.infra.jpa.adapter.partner

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.infra.jpa.core.CouponTemplateCoreRepository
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun findById(id: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(id)
    }
}
