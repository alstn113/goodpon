package com.goodpon.infra.jpa.coupon.adapter

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.jpa.coupon.entity.CouponHistoryEntity
import com.goodpon.infra.jpa.coupon.repository.CouponHistoryJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import com.goodpon.dashboard.application.coupon.port.out.CouponHistoryRepository as Dashboard_CouponHistoryRepository
import com.goodpon.partner.application.coupon.port.out.CouponHistoryRepository as Partner_CouponHistoryRepository

@Repository
class CouponHistoryJpaAdapter(
    private val couponHistoryJpaRepository: CouponHistoryJpaRepository,
) : Dashboard_CouponHistoryRepository, Partner_CouponHistoryRepository {

    override fun save(couponHistory: CouponHistory): CouponHistory {
        val entity = couponHistoryJpaRepository.findByIdOrNull(couponHistory.id)
        if (entity == null) {
            val newEntity = CouponHistoryEntity.fromDomain(couponHistory)
            val savedEntity = couponHistoryJpaRepository.save(newEntity)
            return savedEntity.toDomain()
        }

        entity.update(couponHistory)
        val savedEntity = couponHistoryJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory> {
        return couponHistoryJpaRepository.findByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            .map { it.toDomain() }
    }
}