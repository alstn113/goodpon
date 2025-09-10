package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.db.jpa.entity.CouponHistoryEntity
import com.goodpon.infra.db.jpa.entity.QCouponHistoryEntity.Companion.couponHistoryEntity
import com.goodpon.infra.db.jpa.entity.QCouponTemplateEntity.Companion.couponTemplateEntity
import com.goodpon.infra.db.jpa.entity.QUserCouponEntity.Companion.userCouponEntity
import com.goodpon.infra.db.jpa.repository.CouponHistoryJpaRepository
import com.goodpon.infra.db.jpa.repository.dto.CouponHistoryQueryDto
import com.goodpon.infra.db.jpa.repository.dto.CouponHistorySummaryDto
import com.goodpon.infra.db.jpa.repository.dto.QCouponHistorySummaryDto
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class CouponHistoryCoreRepository(
    private val couponHistoryJpaRepository: CouponHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager
) {

    @Transactional
    fun save(couponHistory: CouponHistory): CouponHistory {
        val entity = couponHistoryJpaRepository.findByIdOrNull(couponHistory.id)
        if (entity == null) {
            val newEntity = CouponHistoryEntity.fromDomain(couponHistory)
            em.persist(newEntity)
            return newEntity.toDomain()
        }

        entity.update(couponHistory)
        return entity.toDomain()
    }

    @Transactional(readOnly = true)
    fun findFirstByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): CouponHistory? {
        return couponHistoryJpaRepository.findFirstByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            ?.toDomain()
    }

    @Transactional(readOnly = true)
    fun findCouponHistories(query: CouponHistoryQueryDto): List<CouponHistorySummaryDto> {
        return queryFactory.select(
            QCouponHistorySummaryDto(
                couponHistoryEntity.id,
                couponHistoryEntity.actionType,
                couponHistoryEntity.recordedAt,
                couponHistoryEntity.orderId,
                couponHistoryEntity.reason,
                userCouponEntity.id,
                userCouponEntity.userId,
                couponTemplateEntity.id,
                couponTemplateEntity.name
            )
        )
            .from(couponHistoryEntity)
            .join(userCouponEntity).on(couponHistoryEntity.userCouponId.eq(userCouponEntity.id))
            .join(couponTemplateEntity).on(userCouponEntity.couponTemplateId.eq(couponTemplateEntity.id))
            .where(
                goeStartDate(query.startDate),
                loeEndDate(query.endDate),
                eqUserId(query.userId),
                eqOrderId(query.orderId),
                eqCouponTemplateId(query.couponTemplateId),
                loe(query.nextCursor)
            )
            .orderBy(couponHistoryEntity.recordedAt.asc(), couponHistoryEntity.id.asc())
            .limit((query.size + 1).toLong())
            .fetch()
    }

    private fun goeStartDate(startDate: LocalDate?): BooleanExpression? {
        return startDate?.let {
            couponHistoryEntity.recordedAt.goe(it.atStartOfDay())
        }
    }

    private fun loeEndDate(endDate: LocalDate?): BooleanExpression? {
        return endDate?.let {
            couponHistoryEntity.recordedAt.lt(it.plusDays(1).atStartOfDay())
        }
    }

    private fun eqUserId(userId: String?): BooleanExpression? {
        return userId?.let {
            userCouponEntity.userId.eq(it)
        }
    }

    private fun eqOrderId(orderId: String?): BooleanExpression? {
        return orderId?.let {
            couponHistoryEntity.orderId.eq(it)
        }
    }

    private fun eqCouponTemplateId(couponTemplateId: Long?): BooleanExpression? {
        return couponTemplateId?.let {
            couponTemplateEntity.id.eq(it)
        }
    }

    private fun loe(nextCursor: String?): BooleanExpression? {
        return nextCursor?.let {
            couponHistoryEntity.id.loe(it)
        }
    }
}