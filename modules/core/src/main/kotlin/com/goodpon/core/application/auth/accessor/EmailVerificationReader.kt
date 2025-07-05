package com.goodpon.core.application.auth.accessor

import com.goodpon.core.domain.auth.exception.EmailVerificationNotFoundException
import com.goodpon.core.domain.auth.EmailVerification
import com.goodpon.core.domain.auth.EmailVerificationRepository
import org.springframework.stereotype.Component

@Component
class EmailVerificationReader(
    private val emailVerificationRepository: EmailVerificationRepository,
) {

    fun readByToken(token: String): EmailVerification {
        return emailVerificationRepository.findByToken(token)
            ?: throw EmailVerificationNotFoundException()
    }
}