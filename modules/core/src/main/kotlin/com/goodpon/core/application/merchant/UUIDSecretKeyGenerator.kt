package com.goodpon.core.application.merchant

import com.goodpon.core.domain.merchant.SecretKeyGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDSecretKeyGenerator : SecretKeyGenerator {
    override fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}