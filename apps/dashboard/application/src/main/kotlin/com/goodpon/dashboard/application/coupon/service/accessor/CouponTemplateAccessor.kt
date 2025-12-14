package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponTemplateAccessor(
    private val couponTemplateRepository: CouponTemplateRepository,
    private val cacheManager: CacheManager,
) {

    @Transactional(readOnly = true)
    fun readByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate> {
        return couponTemplateRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = status,
            absoluteExpiresAt = absoluteExpiresAt
        )
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["couponTemplates:byId"], key = "#couponTemplateId")
    fun readById(couponTemplateId: Long): CouponTemplate {
        return couponTemplateRepository.findById(couponTemplateId)
            ?: throw CouponTemplateNotFoundException()
    }

    @Transactional
    fun save(couponTemplate: CouponTemplate): CouponTemplate {
        return couponTemplateRepository.save(couponTemplate)
    }

    @Transactional
    fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate> {
        val savedCouponTemplates = couponTemplateRepository.saveAll(couponTemplates)
        evictCouponTemplateCaches(couponTemplates.map { it.id })
        return savedCouponTemplates
    }

    private fun evictCouponTemplateCaches(couponTemplateIds: List<Long>) {
        val cache = cacheManager.getCache("couponTemplates:byId") ?: return
        couponTemplateIds.forEach { couponTemplateId ->
            cache.evict(couponTemplateId)
        }
    }
}