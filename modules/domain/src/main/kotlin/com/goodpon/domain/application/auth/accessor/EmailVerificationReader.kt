package com.goodpon.domain.application.auth.accessor

import com.goodpon.domain.domain.auth.EmailVerification
import com.goodpon.domain.domain.auth.EmailVerificationRepository
import com.goodpon.domain.domain.auth.exception.EmailVerificationNotFoundException
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