package com.goodpon.api.partner.idempotency

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

object HashUtil {

    fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(input.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(digest)
    }
}