package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.PublishCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PublishCouponTemplateService(
    private val merchantAccessor: MerchantAccessor,
    private val couponTemplateAccessor: CouponTemplateAccessor,
) : PublishCouponTemplateUseCase {

    @Transactional
    override fun invoke(command: PublishCouponTemplateCommand): PublishCouponTemplateResult {
        val merchant = merchantAccessor.readById(command.merchantId)
        if (!merchant.isAccessibleBy(command.accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        val couponTemplate = couponTemplateAccessor.readById(command.couponTemplateId)
        if (!couponTemplate.isOwnedBy(merchantId = merchant.id)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }

        val publishedCouponTemplate = couponTemplate.publish()
        val savedCouponTemplate = couponTemplateAccessor.save(publishedCouponTemplate)

        return PublishCouponTemplateResult(
            id = savedCouponTemplate.id,
            name = savedCouponTemplate.name,
            merchantId = savedCouponTemplate.merchantId,
            status = savedCouponTemplate.status,
        )
    }
}