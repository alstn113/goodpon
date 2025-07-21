package com.goodpon.application.dashboard.coupon.service

import com.goodpon.application.dashboard.coupon.port.`in`.GetMerchantCouponTemplateDetailUseCase
import com.goodpon.application.dashboard.coupon.port.`in`.dto.GetMerchantCouponTemplateDetailQuery
import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateRepository
import com.goodpon.application.dashboard.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.dashboard.coupon.service.dto.CouponTemplateDetail
import com.goodpon.application.dashboard.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.dashboard.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.application.dashboard.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMerchantCouponTemplateDetailService(
    private val merchantAccessor: MerchantAccessor,
    private val couponTemplateRepository: CouponTemplateRepository,
) : GetMerchantCouponTemplateDetailUseCase {

    @Transactional(readOnly = true)
    override fun invoke(query: GetMerchantCouponTemplateDetailQuery): CouponTemplateDetail {
        val merchant = merchantAccessor.readById(query.merchantId)
        if (!merchant.isAccessibleBy(query.accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        val couponTemplateDetail = couponTemplateRepository
            .findCouponTemplateDetail(couponTemplateId = query.couponTemplateId)
            ?: throw CouponTemplateNotFoundException()

        val isNotOwnedByMerchant = couponTemplateDetail.merchantId != query.merchantId
        if (isNotOwnedByMerchant) {
            throw CouponTemplateNotOwnedByMerchantException()
        }

        return couponTemplateDetail
    }
}