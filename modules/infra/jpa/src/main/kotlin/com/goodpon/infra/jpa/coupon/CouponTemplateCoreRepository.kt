package com.goodpon.infra.jpa.coupon

import com.goodpon.domain.domain.coupon.template.exception.CouponTemplateNotFoundException
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateRepository
import com.goodpon.domain.domain.coupon.template.vo.CouponTemplateStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CouponTemplateCoreRepository(
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
) : com.goodpon.domain.coupon.template.CouponTemplateRepository {

    override fun save(couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate): com.goodpon.domain.coupon.template.CouponTemplate {
        if (couponTemplate.id == 0L) {
            val entity = CouponTemplateEntity.fromDomain(couponTemplate)
            val savedEntity = couponTemplateJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = couponTemplateJpaRepository.findByIdOrNull(couponTemplate.id)
            ?: throw CouponTemplateNotFoundException()
        entity.update(couponTemplate)
        val savedEntity = couponTemplateJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun saveAll(couponTemplates: List<com.goodpon.domain.coupon.template.CouponTemplate>): List<com.goodpon.domain.coupon.template.CouponTemplate> {
        val entities = couponTemplates.map { CouponTemplateEntity.fromDomain(it) }
        val savedEntities = couponTemplateJpaRepository.saveAll(entities)
        return savedEntities.map { it.toDomain() }
    }

    override fun findById(id: Long): com.goodpon.domain.coupon.template.CouponTemplate? {
        return couponTemplateJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    override fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<com.goodpon.domain.coupon.template.CouponTemplate> {
        return couponTemplateJpaRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = status,
            absoluteExpiresAt = absoluteExpiresAt,
        ).map { it.toDomain() }
    }
}