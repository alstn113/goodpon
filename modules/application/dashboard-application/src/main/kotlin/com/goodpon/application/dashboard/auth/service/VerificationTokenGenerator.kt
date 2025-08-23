package com.goodpon.application.dashboard.auth.service

import org.springframework.stereotype.Component
import java.util.*

@Component
interface VerificationTokenGenerator {

    fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}