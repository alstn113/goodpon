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
        val (couponTemplateId, userId, requestedAt) = command

        publishCouponIssuedEvent(userId, couponTemplateId)

        // 이미 발급된 쿠폰도 정합성 위해 이벤트를 발행하여 reserved -> issued 처리
        if (userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)) {
            log.warn("이미 발급된 쿠폰이 요청되었습니다. templateId=$couponTemplateId, userId=$userId")
            return
        }

        val couponTemplate = couponTemplateAccessor.readById(couponTemplateId)
        val userCoupon = CouponIssuer.issue(couponTemplate, userId, requestedAt)

        val savedUserCoupon = userCouponAccessor.save(userCoupon)
        couponHistoryAccessor.recordIssued(savedUserCoupon.id, requestedAt)
    }

    private fun publishCouponIssuedEvent(userId: String, templateId: Long) {
        val event = CouponIssuedEvent(
            userId = userId,
            couponTemplateId = templateId
        )
        eventPublisher.publishEvent(event)
    }
}
