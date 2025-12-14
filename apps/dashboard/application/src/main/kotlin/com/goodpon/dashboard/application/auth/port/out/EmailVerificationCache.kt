package com.goodpon.dashboard.application.auth.port.out

import com.goodpon.dashboard.application.auth.port.out.dto.EmailVerificationDto

interface EmailVerificationCache {

    fun save(verification: EmailVerificationDto, ttlMinutes: Long = 30)

    fun findByToken(token: String): EmailVerificationDto?

    fun delete(token: String, accountId: Long)
}
