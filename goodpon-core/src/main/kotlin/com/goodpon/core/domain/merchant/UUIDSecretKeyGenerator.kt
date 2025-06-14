package com.goodpon.core.domain.merchant

import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDSecretKeyGenerator : SecretKeyGenerator {

    override fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}