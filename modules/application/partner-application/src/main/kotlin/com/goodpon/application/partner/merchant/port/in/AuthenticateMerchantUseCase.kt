package com.goodpon.application.partner.merchant.port.`in`

import com.goodpon.application.partner.merchant.port.`in`.dto.MerchantInfo

fun interface AuthenticateMerchantUseCase {

    operator fun invoke(clientId: String, clientSecret: String): MerchantInfo
}