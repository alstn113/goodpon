package com.goodpon.partner.application.auth.accessor

import com.goodpon.domain.auth.EmailVerificationRepository
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