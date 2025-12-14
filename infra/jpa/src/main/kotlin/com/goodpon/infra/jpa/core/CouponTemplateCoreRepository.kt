package com.goodpon.infra.jpa.core

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.jpa.entity.CouponTemplateEntity
import com.goodpon.infra.jpa.repository.CouponTemplateJpaRepository
import com.goodpon.infra.jpa.repository.dto.CouponTemplateDetailDto
import com.goodpon.infra.jpa.repository.dto.CouponTemplateDetailWithStatsDto
import com.goodpon.infra.jpa.repository.dto.CouponTemplateSummaryDto
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class CouponTemplateCoreRepository(
    private val couponTemplateJpaRepository: CouponTemplateJpaRepository,
    private val em: EntityManager,
) {

    @Transactional
    fun save(couponTemplate: CouponTemplate): CouponTemplate {
        if (couponTemplate.id == 0L) {
            val entity = CouponTemplateEntity.fromDomain(couponTemplate)
            em.persist(entity)
            return entity.toDomain()
        }

        val entity = couponTemplateJpaRepository.findByIdOrNull(couponTemplate.id)
            ?: throw EntityNotFoundException()
        entity.update(couponTemplate)
        return entity.toDomain()
    }

    @Transactional
    fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate> {
        val entities = couponTemplates.map { CouponTemplateEntity.fromDomain(it) }
        return couponTemplateJpaRepository.saveAll(entities)
            .map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): CouponTemplate? {
        return couponTemplateJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    @Transactional(readOnly = true)
    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate> {
        return couponTemplateJpaRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = status,
            absoluteExpiresAt = absoluteExpiresAt,
        ).map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    fun findCouponTemplateSummaries(merchantId: Long): List<CouponTemplateSummaryDto> {
        return couponTemplateJpaRepository.findCouponTemplateSummaries(merchantId)
    }

    @Transactional(readOnly = true)
    fun findByIdWithStats(couponTemplateId: Long): CouponTemplateDetailWithStatsDto? {
        return couponTemplateJpaRepository.findByIdWithStats(couponTemplateId)
    }

    @Transactional(readOnly = true)
    fun findDetailById(couponTemplateId: Long, merchantId: Long): CouponTemplateDetailDto? {
        return couponTemplateJpaRepository.findDetailById(couponTemplateId, merchantId)
    }
}