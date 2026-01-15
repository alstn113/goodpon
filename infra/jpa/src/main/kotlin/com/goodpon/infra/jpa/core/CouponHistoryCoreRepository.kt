package com.goodpon.infra.jpa.core

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.jpa.entity.CouponHistoryEntity
import com.goodpon.infra.jpa.entity.QCouponHistoryEntity.Companion.couponHistoryEntity
import com.goodpon.infra.jpa.entity.QCouponTemplateEntity.Companion.couponTemplateEntity
import com.goodpon.infra.jpa.entity.QUserCouponEntity.Companion.userCouponEntity
import com.goodpon.infra.jpa.repository.CouponHistoryJpaRepository
import com.goodpon.infra.jpa.repository.dto.CouponHistoryQueryDto
import com.goodpon.infra.jpa.repository.dto.CouponHistorySummaryDto
import com.goodpon.infra.jpa.repository.dto.QCouponHistorySummaryDto
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class CouponHistoryCoreRepository(
    private val couponHistoryJpaRepository: CouponHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager,
    private val jdbcTemplate: JdbcTemplate,
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

    @Transactional
    fun saveAll(couponHistories: List<CouponHistory>) {
        val entities = couponHistories.map { CouponHistoryEntity.fromDomain(it) }

        val now = Timestamp.valueOf(LocalDateTime.now())

        val sql = """
        INSERT INTO coupon_histories 
        (id, user_coupon_id, action_type, order_id, reason, recorded_at, created_at, updated_at) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()

        jdbcTemplate.batchUpdate(
            sql,
            entities,
            entities.size
        ) { ps, entity ->
            ps.setString(1, entity.id)
            ps.setString(2, entity.userCouponId)
            ps.setString(3, entity.actionType.name)
            ps.setString(4, entity.orderId) // setString 은 null 값을 허용합니다
            ps.setString(5, entity.reason) // setString 은 null 값을 허용합니다
            ps.setTimestamp(6, Timestamp.valueOf(entity.recordedAt))
            ps.setTimestamp(7, now)
            ps.setTimestamp(8, now)
        }
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