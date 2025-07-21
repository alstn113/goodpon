package com.goodpon.application.dashboard.auth.port.out

import com.goodpon.application.dashboard.auth.port.out.dto.EmailVerificationDto

interface EmailVerificationCache {

    fun save(verification: EmailVerificationDto, ttlMinutes: Long = 30)

    fun findByToken(token: String): EmailVerificationDto?

    fun delete(token: String, accountId: Long)
}
