package com.goodpon.couponissuer.infra.persistence

import com.goodpon.couponissuer.application.port.out.CouponTemplateRepository
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.infra.jpa.core.CouponTemplateCoreRepository
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun findById(couponTemplateId: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(couponTemplateId)
    }
}