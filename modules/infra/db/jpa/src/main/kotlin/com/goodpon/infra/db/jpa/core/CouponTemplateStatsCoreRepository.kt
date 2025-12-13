package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import com.goodpon.infra.db.jpa.repository.CouponTemplateStatsJpaRepository
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.PreparedStatement

@Component
class CouponTemplateStatsCoreRepository(
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val em: EntityManager,
) {

    @Transactional
    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        val entity = couponTemplateStatsJpaRepository.findByIdOrNull(couponTemplateStats.couponTemplateId)
        if (entity == null) {
            val newEntity = CouponTemplateStatsEntity.fromDomain(couponTemplateStats)
            em.persist(newEntity)
            return newEntity.toDomain()
        }

        entity.update(couponTemplateStats)
        return entity.toDomain()
    }

    @Transactional
    fun batchUpdateCouponTemplateStats(statsUpdates: Map<Long, Pair<Long, Long>>) {
        val sql = """
            UPDATE coupon_template_stats
            SET issue_count = ?, redeem_count = ?
            WHERE coupon_template_id = ?
        """.trimIndent()

        val entriesList = statsUpdates.entries.toList()

        jdbcTemplate.batchUpdate(
            sql,
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val (couponTemplateId, counts) = entriesList[i]
                    val (issueCount, redeemCount) = counts
                    ps.setLong(1, issueCount)
                    ps.setLong(2, redeemCount)
                    ps.setLong(3, couponTemplateId)
                }

                override fun getBatchSize(): Int = statsUpdates.size
            }
        )
    }
}