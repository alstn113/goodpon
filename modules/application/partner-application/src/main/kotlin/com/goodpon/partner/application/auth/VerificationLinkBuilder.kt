package com.goodpon.partner.application.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class VerificationLinkBuilder(
    @Value("\${client-host}") private val baseUrl: String,
) {

    fun build(token: String): String {
        return "$baseUrl/verify?token=$token"
    }
}