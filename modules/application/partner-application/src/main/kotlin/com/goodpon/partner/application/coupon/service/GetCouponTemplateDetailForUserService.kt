package com.goodpon.partner.application.coupon.service

import com.goodpon.partner.application.coupon.port.`in`.GetCouponTemplateDetailForUserUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.CouponIssuanceStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.GetCouponTemplateDetailForUserQuery
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetailForUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Service
class GetCouponTemplateDetailForUserService(
    private val couponTemplateRepository: CouponTemplateRepository,
    private val userCouponRepository: UserCouponRepository,
) : GetCouponTemplateDetailForUserUseCase {

    @Transactional(readOnly = true)
    override fun invoke(query: GetCouponTemplateDetailForUserQuery): CouponTemplateDetailForUser {
        val now = LocalDateTime.now()

        val detail = couponTemplateRepository.findCouponTemplateDetail(
            couponTemplateId = query.couponTemplateId,
            merchantId = query.merchantId
        ) ?: throw CouponTemplateNotFoundException()

        val issuanceStatus: CouponIssuanceStatus = when {
            now < detail.issueStartAt -> CouponIssuanceStatus.PERIOD_NOT_STARTED
            detail.issueEndAt != null && detail.issueEndAt < now -> CouponIssuanceStatus.PERIOD_ENDED
            checkAlreadyIssued(query.userId, query.couponTemplateId) -> CouponIssuanceStatus.ALREADY_ISSUED_BY_USER
            detail.maxIssueCount != null && detail.issueCount >= detail.maxIssueCount -> CouponIssuanceStatus.MAX_ISSUE_COUNT_EXCEEDED
            detail.maxRedeemCount != null && detail.redeemCount >= detail.maxRedeemCount -> CouponIssuanceStatus.MAX_REDEEM_COUNT_EXCEEDED
            else -> CouponIssuanceStatus.AVAILABLE
        }

        return detail.forUser(issuanceStatus = issuanceStatus)
    }

    private fun checkAlreadyIssued(userId: String?, couponTemplateId: Long): Boolean {
        return userId != null && userCouponRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}
