package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponHistoryRepository
import com.goodpon.dashboard.application.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponHistorySummary
import com.goodpon.dashboard.application.coupon.service.dto.GetCouponHistoriesQuery
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.db.jpa.core.CouponHistoryCoreRepository
import com.goodpon.infra.db.jpa.repository.dto.CouponHistoryQueryDto
import org.springframework.stereotype.Repository

@Repository("dashboardCouponHistoryJpaAdapter")
class CouponHistoryJpaAdapter(
    private val couponHistoryCoreRepository: CouponHistoryCoreRepository,
) : CouponHistoryRepository {

    override fun save(couponHistory: CouponHistory): CouponHistory {
        return couponHistoryCoreRepository.save(couponHistory)
    }

    override fun findCouponHistories(query: GetCouponHistoriesQuery): CouponHistoryQueryResult {
        val queryDto = CouponHistoryQueryDto(
            startDate = query.startDate,
            endDate = query.endDate,
            userId = query.userId,
            orderId = query.orderId,
            couponTemplateId = query.couponTemplateId,
            nextCursor = query.nextCursor,
            size = query.size
        )
        val fetchedList = couponHistoryCoreRepository.findCouponHistories(queryDto)

        val hasMore = fetchedList.size > query.size
        val nextCursor = if (hasMore) {
            fetchedList.lastOrNull()?.historyId
        } else {
            null
        }
        val content = if (hasMore) {
            fetchedList.dropLast(1)
        } else {
            fetchedList
        }.map {
            CouponHistorySummary(
                historyId = it.historyId,
                actionType = it.actionType,
                recordedAt = it.recordedAt,
                orderId = it.orderId,
                reason = it.reason,
                userCouponId = it.userCouponId,
                userId = it.userId,
                couponTemplateId = it.couponTemplateId,
                couponTemplateName = it.couponTemplateName
            )
        }

        return CouponHistoryQueryResult(
            content = content,
            size = content.size,
            hasMore = hasMore,
            nextCursor = nextCursor
        )
    }
}
