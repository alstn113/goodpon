package com.goodpon.couponissuer.application.service

import com.goodpon.couponissuer.application.port.`in`.IssueCouponUseCase
import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.application.service.accessor.CouponHistoryAccessor
import com.goodpon.couponissuer.application.service.accessor.CouponTemplateAccessor
import com.goodpon.couponissuer.application.service.accessor.UserCouponAccessor
import com.goodpon.couponissuer.application.service.listener.CouponIssuedEvent
import com.goodpon.domain.coupon.service.CouponIssuer
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun invoke(command: IssueCouponCommand) {
        publishCouponIssuedEvent(command = command)

        val couponTemplate = couponTemplateAccessor.readById(command.couponTemplateId)
        if (userCouponAccessor.existsByUserIdAndCouponTemplateId(command.userId, command.couponTemplateId)) {
            log.warn("이미 발급된 쿠폰이 요청되었습니다. couponTemplateId={}, userId={}", command.couponTemplateId, command.userId)
            return
        }

        val userCoupon = CouponIssuer.issue(couponTemplate, userId = command.userId, issueAt = command.requestedAt)
        val savedUserCoupon = userCouponAccessor.save(userCoupon)

        couponHistoryAccessor.recordIssued(userCouponId = savedUserCoupon.id, recordedAt = command.requestedAt)
    }

    private fun publishCouponIssuedEvent(command: IssueCouponCommand) {
        val event = CouponIssuedEvent(
            userId = command.userId,
            couponTemplateId = command.couponTemplateId
        )
        eventPublisher.publishEvent(event)
    }
}
