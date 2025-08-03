package com.goodpon.api.partner.idempotency

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.partner.idempotency.exception.IdempotencyRequestPayloadMismatchException
import com.goodpon.api.partner.idempotency.exception.IdempotencyRequestProcessingException
import com.goodpon.api.partner.idempotency.exception.InvalidIdempotencyKeyException
import com.goodpon.api.partner.security.filter.CachedBodyHttpServletRequest
import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

class IdempotencyInterceptor(
    private val idempotencyUseCase: IdempotencyUseCase,
    private val objectMapper: ObjectMapper,
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (!isIdempotentHandler(handler)) return true

        val req = request as CachedBodyHttpServletRequest
        val res = response as ContentCachingResponseWrapper

        val idempotencyKey = req.getHeader(IDEMPOTENCY_KEY_HEADER)
            ?.also { res.setHeader(IDEMPOTENCY_KEY_HEADER, it) }
            ?: return true

        validateKeyLength(idempotencyKey)

        val key = generateKey(req, idempotencyKey)
        val hashedRequest = hash(getRequestBody(req))

        return when (val result = idempotencyUseCase.validateKey(key, hashedRequest)) {
            is IdempotencyCheckResult.NotFound -> {
                idempotencyUseCase.markAsProcessing(key, hashedRequest)
                true
            }

            is IdempotencyCheckResult.Conflict -> throw IdempotencyRequestPayloadMismatchException()
            is IdempotencyCheckResult.Processing -> throw IdempotencyRequestProcessingException()

            is IdempotencyCheckResult.Completed -> {
                writeStoredResponseToClient(res, result.response)
                false
            }
        }
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        if (!isIdempotentHandler(handler)) return

        val req = request as CachedBodyHttpServletRequest
        val res = response as ContentCachingResponseWrapper

        val idempotencyKey = req.getHeader(IDEMPOTENCY_KEY_HEADER) ?: return
        val key = generateKey(req, idempotencyKey)

        val body = objectMapper.readTree(res.contentAsByteArray)
        val headers = res.headerNames.associateWith { res.getHeaders(it).toList() }

        val idempotencyResponse = IdempotencyResponse(
            status = res.status,
            headers = headers,
            body = body,
        )

        idempotencyUseCase.markAsCompleted(key, idempotencyResponse)
    }

    private fun isIdempotentHandler(handler: Any): Boolean =
        handler is HandlerMethod && handler.hasMethodAnnotation(Idempotent::class.java)

    private fun validateKeyLength(key: String) {
        if (key.length > IDEMPOTENCY_KEY_LENGTH_LIMIT) {
            throw InvalidIdempotencyKeyException()
        }
    }

    private fun generateKey(request: CachedBodyHttpServletRequest, idempotencyKey: String): String {
        val clientId = request.getHeader(GOODPON_CLIENT_ID_HEADER)!!
        val raw = "$idempotencyKey:$clientId:${request.method}:${request.requestURI}"
        return "idempotency:${hash(raw)}"
    }

    private fun getRequestBody(request: CachedBodyHttpServletRequest): String {
        return String(request.getCachedBody(), StandardCharsets.UTF_8)
    }

    private fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(input.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(digest)
    }

    private fun writeStoredResponseToClient(
        response: ContentCachingResponseWrapper,
        stored: IdempotencyResponse,
    ) {
        response.status = stored.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()

        stored.headers.forEach { (name, values) ->
            values.forEach { value -> response.addHeader(name, value) }
        }

        response.writer.write(objectMapper.writeValueAsString(stored.body))
        response.copyBodyToResponse()
    }

    companion object {
        private const val IDEMPOTENCY_KEY_HEADER = "Idempotency-Key"
        private const val GOODPON_CLIENT_ID_HEADER = "X-Goodpon-Client-Id"
        private const val IDEMPOTENCY_KEY_LENGTH_LIMIT = 300
    }
}
