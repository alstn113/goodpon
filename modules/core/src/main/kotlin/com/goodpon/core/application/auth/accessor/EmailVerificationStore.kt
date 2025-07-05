package com.goodpon.core.application.auth.accessor

import com.goodpon.core.domain.auth.EmailVerificationRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EmailVerificationStore(
    private val emailVerificationRepository: EmailVerificationRepository,
) {

    @Transactional
    fun delete(token: String, accountId: Long) {
        emailVerificationRepository.delete(token = token, accountId = accountId)
    }
}