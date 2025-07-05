package com.goodpon.core.application.merchant

import com.goodpon.core.domain.merchant.SecretKeyGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UuidSecretKeyGenerator : SecretKeyGenerator {

    override fun generate(): String {
        val uuid = UUID.randomUUID().toString().replace("-", "")
        return "$SECRET_KEY_PREFIX$uuid"
    }

    companion object {
        private const val SECRET_KEY_PREFIX = "sk_"
    }
}