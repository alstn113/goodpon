package com.goodpon.domain.merchant

import java.time.LocalDateTime

data class MerchantClientSecret private constructor(
    val id: Long = 0,
    val secret: String,
    val expiredAt: LocalDateTime?,
) {

    fun expire(expiredAt: LocalDateTime): MerchantClientSecret {
        return this.copy(expiredAt = expiredAt)
    }

    fun isExpired(): Boolean {
        return expiredAt != null
    }

    companion object {
        fun create(secret: String): MerchantClientSecret {
            return MerchantClientSecret(
                secret = secret,
                expiredAt = null
            )
        }

        fun reconstruct(id: Long, secret: String, expiredAt: LocalDateTime?): MerchantClientSecret {
            return MerchantClientSecret(
                id = id,
                secret = secret,
                expiredAt = expiredAt
            )
        }
    }
}