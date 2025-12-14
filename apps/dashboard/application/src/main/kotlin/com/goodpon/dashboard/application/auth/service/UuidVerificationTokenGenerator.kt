package com.goodpon.dashboard.application.auth.service

import org.springframework.stereotype.Component
import java.util.*

@Component
class UuidVerificationTokenGenerator : VerificationTokenGenerator {

    override fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}