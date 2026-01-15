package com.goodpon.couponissuer.application.service

import com.goodpon.couponissuer.application.port.`in`.IssueCouponBatchUseCase
import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.application.service.accessor.CouponHistoryAccessor
import com.goodpon.couponissuer.application.service.accessor.CouponTemplateAccessor
import com.goodpon.couponissuer.application.service.accessor.UserCouponAccessor
import com.goodpon.couponissuer.application.service.listener.CouponIssuedEvent
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.domain.coupon.service.CouponIssuer
import com.goodpon.domain.coupon.user.UserCoupon
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueCouponBatchService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val userCouponAccessor: UserCouponAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val eventPublisher: ApplicationEventPublisher,
) : IssueCouponBatchUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun invoke(commands: List<IssueCouponCommand>) {
        if (commands.isEmpty()) {
            return
        }

        val templateIds = commands.map { it.couponTemplateId }.distinct()
        val templatesMap = couponTemplateAccessor.readAllByIdIn(templateIds)
            .associateBy { it.id }

        val userIds = commands.map { it.userId }
        val issuedCoupons = userCouponAccessor.readAllByUserIdInAndTemplateIdIn(userIds, templateIds)
        val issuedKeys = issuedCoupons.map { "${it.userId}:${it.couponTemplateId}" }.toSet()

        val couponsToInsert = ArrayList<UserCoupon>()
        val historiesToInsert = ArrayList<CouponHistory>()

        commands.forEach { cmd ->
            val key = "${cmd.userId}:${cmd.couponTemplateId}"

            val template = templatesMap[cmd.couponTemplateId]
            if (template == null) {
                log.error("존재하지 않는 템플릿 ID 요청입니다. userId=${cmd.userId}, templateId=${cmd.couponTemplateId}")
                return@forEach
            }

            // 이미 발급된 쿠폰인 경우에도 Redis 확정 처리를 위해 이벤트 발행
            publishCouponIssuedEvent(cmd.userId, cmd.couponTemplateId)

            if (issuedKeys.contains(key)) {
                log.warn("이미 발급된 쿠폰입니다. 무시합니다. userId=${cmd.userId}, templateId=${cmd.couponTemplateId}")
                return@forEach
            }

            val newCoupon = CouponIssuer.issue(template, cmd.userId, cmd.requestedAt)
            couponsToInsert.add(newCoupon)
            historiesToInsert.add(CouponHistory.issued(newCoupon.id, cmd.requestedAt))
        }

        if (couponsToInsert.isNotEmpty()) {
            userCouponAccessor.bulkInsertCoupons(couponsToInsert)
            couponHistoryAccessor.bulkInsertHistories(historiesToInsert)
        }
    }

    private fun publishCouponIssuedEvent(userId: String, templateId: Long) {
        val event = CouponIssuedEvent(
            userId = userId,
            couponTemplateId = templateId
        )
        eventPublisher.publishEvent(event)
    }
}