package com.goodpon.core.domain.merchant

import java.util.*

data class Merchant private constructor(
    val id: Long = 0,
    val name: String,
    val secretKey: String,
) {

    companion object {
        fun create(name: String): Merchant {
            return Merchant(
                name = name,
                secretKey = generateSecretKey()
            )
        }

        fun reconstruct(id: Long, name: String, secretKey: String): Merchant {
            return Merchant(
                id = id,
                name = name,
                secretKey = secretKey
            )
        }

        private fun generateSecretKey(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }
    }
}