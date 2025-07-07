package com.goodpon.infra.jpa.coupon.adapter

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateNotFoundException
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.jpa.coupon.entity.CouponTemplateEntity
import com.goodpon.infra.jpa.coupon.repository.CouponTemplateJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository as Dashboard_CouponTemplateRepository
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository as Partner_CouponTemplateRepository


@Repository
class CouponTemplateJpaAdapter(
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
) : Partner_CouponTemplateRepository, Dashboard_CouponTemplateRepository {

    override fun save(couponTemplate: CouponTemplate): CouponTemplate {
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

    override fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate> {
        val entities = couponTemplates.map { CouponTemplateEntity.fromDomain(it) }
        val savedEntities = couponTemplateJpaRepository.saveAll(entities)
        return savedEntities.map { it.toDomain() }
    }

    override fun findById(id: Long): CouponTemplate? {
        return couponTemplateJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    override fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate> {
        return couponTemplateJpaRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = status,
            absoluteExpiresAt = absoluteExpiresAt,
        ).map { it.toDomain() }
    }
}