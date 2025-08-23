package com.goodpon.application.couponissuer.service

import com.goodpon.application.couponissuer.port.`in`.IssueCouponUseCase
import com.goodpon.application.couponissuer.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.couponissuer.service.accessor.CouponHistoryAccessor
import com.goodpon.application.couponissuer.service.accessor.CouponTemplateAccessor
import com.goodpon.application.couponissuer.service.accessor.UserCouponAccessor
import com.goodpon.application.couponissuer.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.application.couponissuer.service.listener.IssueCouponRollbackEvent
import com.goodpon.domain.coupon.service.CouponIssuer
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val userCouponAccessor: UserCouponAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val eventPublisher: ApplicationEventPublisher,
) : IssueCouponUseCase {

    @Transactional
    override fun invoke(command: IssueCouponCommand) {
        publishIssueCouponRollbackEvent(command)

        val couponTemplate = couponTemplateAccessor.readById(command.couponTemplateId)
        validateAlreadyIssued(userId = command.userId, couponTemplateId = command.couponTemplateId)

        val userCoupon = CouponIssuer.issue(couponTemplate, userId = command.userId, issueAt = command.requestedAt)
        val savedUserCoupon = userCouponAccessor.save(userCoupon)

        couponHistoryAccessor.recordIssued(userCouponId = savedUserCoupon.id, recordedAt = command.requestedAt)
    }

    private fun publishIssueCouponRollbackEvent(command: IssueCouponCommand) {
        val event = IssueCouponRollbackEvent(userId = command.userId, couponTemplateId = command.couponTemplateId)
        eventPublisher.publishEvent(event)
    }

    private fun validateAlreadyIssued(userId: String, couponTemplateId: Long) {
        if (userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)) {
            throw UserCouponAlreadyIssuedException()
        }
    }
}
