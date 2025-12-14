package com.goodpon.partner.application.merchant.port.`in`

import com.goodpon.partner.application.merchant.port.`in`.dto.MerchantInfo

fun interface AuthenticateMerchantUseCase {

    operator fun invoke(clientId: String, clientSecret: String): MerchantInfo
}