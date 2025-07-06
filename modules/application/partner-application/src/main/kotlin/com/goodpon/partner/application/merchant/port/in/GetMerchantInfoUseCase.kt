package com.goodpon.partner.application.merchant.port.`in`

import com.goodpon.partner.application.merchant.port.`in`.dto.MerchantInfo

interface GetMerchantInfoUseCase {

    fun getMerchantInfoBySecretKey(secretKey: String): MerchantInfo?
}