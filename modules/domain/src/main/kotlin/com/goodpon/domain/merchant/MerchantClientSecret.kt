package com.goodpon.domain.merchant

import java.time.LocalDateTime

data class MerchantClientSecret private constructor(
    val id: Long = 0,
    val key: String,
    val expiredAt: LocalDateTime?,
) {

    companion object {
        fun create(key: String): MerchantClientSecret {
            return MerchantClientSecret(
                key = key,
                expiredAt = null
            )
        }

        fun reconstruct(id: Long, key: String, expiredAt: LocalDateTime?): MerchantClientSecret {
            return MerchantClientSecret(
                id = id,
                key = key,
                expiredAt = expiredAt
            )
        }
    }
}