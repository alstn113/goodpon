package com.goodpon.partner.application.coupon.service

import com.goodpon.partner.application.coupon.port.`in`.GetCouponTemplateDetailForUserUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.GetCouponTemplateDetailForUserQuery
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetailForUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCouponTemplateDetailForUserService(
    private val couponTemplateRepository: CouponTemplateRepository,
    private val userCouponRepository: UserCouponRepository,
) : GetCouponTemplateDetailForUserUseCase {

    @Transactional(readOnly = true)
    override fun invoke(query: GetCouponTemplateDetailForUserQuery): CouponTemplateDetailForUser {
        val detail = couponTemplateRepository.findCouponTemplateDetail(
            couponTemplateId = query.couponTemplateId,
            merchantId = query.merchantId
        ) ?: throw CouponTemplateNotFoundException()

        val alreadyIssued: Boolean? = query.userId?.let {
            userCouponRepository.existsByUserIdAndCouponTemplateId(
                userId = it,
                couponTemplateId = query.couponTemplateId
            )
        }

        val isIssuable: Boolean = when (detail.maxIssueCount) {
            null -> true
            else -> detail.issueCount < detail.maxIssueCount
        }

        return detail.forUser(alreadyIssued = alreadyIssued, isIssuable = isIssuable)
    }
}
