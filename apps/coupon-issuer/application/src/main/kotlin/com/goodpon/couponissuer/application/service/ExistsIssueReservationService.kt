package com.goodpon.couponissuer.application.service

import com.goodpon.couponissuer.application.port.`in`.ExistsIssueReservationUseCase
import com.goodpon.couponissuer.application.port.out.CouponIssueStore
import org.springframework.stereotype.Service

@Service
class ExistsIssueReservationService(
    private val couponIssueStore: CouponIssueStore,
) : ExistsIssueReservationUseCase {

    override fun invoke(couponTemplateId: Long, userId: String): Boolean {
        return couponIssueStore.existsIssueReservation(couponTemplateId, userId)
    }
}