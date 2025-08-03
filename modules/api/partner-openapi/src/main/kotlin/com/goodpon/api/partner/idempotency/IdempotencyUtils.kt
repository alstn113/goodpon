package com.goodpon.api.partner.idempotency

import com.goodpon.api.partner.idempotency.exception.InvalidIdempotencyKeyException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.util.ContentCachingRequestWrapper
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

object IdempotencyUtils {

    const val IDEMPOTENCY_KEY_HEADER = "Idempotency-Key"
    private const val GOODPON_CLIENT_ID_HEADER = "X-Goodpon-Client-Id"
    private const val IDEMPOTENCY_KEY_LENGTH_LIMIT = 300

    fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(input.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(digest)
    }

    fun getIdempotencyKey(request: HttpServletRequest): String? {
        return request.getHeader(IDEMPOTENCY_KEY_HEADER)
    }

    fun validateIdempotencyKeyLength(idempotencyKey: String) {
        if (idempotencyKey.length > IDEMPOTENCY_KEY_LENGTH_LIMIT) {
            throw InvalidIdempotencyKeyException()
        }
    }

    fun generateKey(request: HttpServletRequest, idempotencyKey: String): String {
        val clientId = request.getHeader(GOODPON_CLIENT_ID_HEADER)!!
        val method = request.method
        val uri = request.requestURI
        val rawKey = "$idempotencyKey:$clientId:$method:$uri"
        return "idempotency:${hash(rawKey)}"
    }

    fun getRequestBody(requestWrapper: ContentCachingRequestWrapper): String {
        return  String(requestWrapper.contentAsByteArray, StandardCharsets.UTF_8)
    }
}