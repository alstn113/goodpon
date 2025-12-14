package com.goodpon.couponissuer.application.service

import com.goodpon.couponissuer.application.port.`in`.MarkIssueReservationAsFailedUseCase
import com.goodpon.couponissuer.application.port.out.CouponIssueStore
import org.springframework.stereotype.Service

@Service
class MarkIssueReservationAsFailedService(
    private val couponIssueStore: CouponIssueStore,
) : MarkIssueReservationAsFailedUseCase {

    override fun invoke(couponTemplateId: Long, userId: String) {
        couponIssueStore.markIssueReservationAsFailed(couponTemplateId = couponTemplateId, userId = userId)
    }
}