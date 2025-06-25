package com.goodpon.core.domain.auth

import org.springframework.stereotype.Component
import java.util.*

@Component
class VerificationTokenGenerator {

    fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}