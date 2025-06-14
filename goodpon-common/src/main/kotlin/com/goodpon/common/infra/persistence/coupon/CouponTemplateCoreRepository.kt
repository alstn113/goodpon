package com.goodpon.common.infra.persistence.coupon

import com.goodpon.common.domain.coupon.CouponTemplate
import com.goodpon.common.domain.coupon.CouponTemplateRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateCoreRepository(
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
) : CouponTemplateRepository {

    fun save(couponTemplate: CouponTemplate): CouponTemplate {
        if (couponTemplate.id == 0L) {
            val entity = CouponTemplateEntity.fromDomain(couponTemplate)
            val savedEntity = couponTemplateJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = couponTemplateJpaRepository.findByIdOrNull(couponTemplate.id)
            ?: throw IllegalArgumentException("CouponTemplate with id ${couponTemplate.id} not found")
        entity.update(couponTemplate)
        val savedEntity = couponTemplateJpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}