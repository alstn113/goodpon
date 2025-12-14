package com.goodpon.dashboard.bootstrap.support

import com.goodpon.dashboard.application.auth.service.VerificationTokenGenerator
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Primary
@Profile("test")
@Component
class FakeVerificationTokenGenerator : VerificationTokenGenerator {

    override fun generate(): String {
        return VERIFICATION_TOKEN
    }

    companion object {
        const val VERIFICATION_TOKEN = "verification-token"
    }
}