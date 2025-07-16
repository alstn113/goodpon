package com.goodpon.partner.application.merchant.port.`in`

import com.goodpon.partner.application.merchant.port.`in`.dto.MerchantInfo

interface AuthenticateMerchantUseCase {

    fun authenticateMerchant(clientId: String, clientSecret: String): MerchantInfo?
}