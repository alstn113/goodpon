package com.goodpon.application.dashboard.merchant.port.`in`

import com.goodpon.application.dashboard.merchant.service.dto.MyMerchantDetail

fun interface GetMyMerchantDetailUseCase {

    operator fun invoke(accountId: Long, merchantId: Long): MyMerchantDetail
}