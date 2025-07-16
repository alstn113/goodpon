package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantDetail

fun interface GetMyMerchantDetailUseCase {

    operator fun invoke(accountId: Long, merchantId: Long): MyMerchantDetail
}