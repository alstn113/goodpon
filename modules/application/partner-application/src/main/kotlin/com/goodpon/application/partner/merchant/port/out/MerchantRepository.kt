package com.goodpon.application.partner.merchant.port.out

import com.goodpon.domain.merchant.Merchant

interface MerchantRepository {

    fun findByClientId(clientId: String): Merchant?
}