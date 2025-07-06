package com.goodpon.dashboard.application.auth.service.accessor

import com.goodpon.dashboard.application.auth.port.out.EmailVerificationRepository
import org.springframework.stereotype.Component

@Component
class EmailVerificationStore(
    private val emailVerificationRepository: EmailVerificationRepository,
) {

    fun delete(token: String, accountId: Long) {
        emailVerificationRepository.delete(token = token, accountId = accountId)
    }
}