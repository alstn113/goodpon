package com.goodpon.api.partner.idempotency

import com.goodpon.api.partner.idempotency.exception.IdempotencyConflictException
import com.goodpon.api.partner.idempotency.exception.IdempotencyProcessingException
import com.goodpon.api.partner.idempotency.exception.InvalidIdempotencyKeyException
import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class IdempotencyAspect(
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val idempotencyUseCase: IdempotencyUseCase,
) {

    @Around("@annotation(Idempotent)")
    fun around(joinPoint: ProceedingJoinPoint): Any {
        val idempotencyKey = request.getHeader(IDEMPOTENCY_KEY_HEADER)
        if (idempotencyKey.isNullOrBlank()) {
            return joinPoint.proceed()
        } else {
            response.setHeader(IDEMPOTENCY_KEY_HEADER, idempotencyKey)
        }

        validateIdempotencyKeyLength(idempotencyKey)

        val key = generateKey(request, idempotencyKey)
        val hashRequest = HashUtil.hash(getRequestBody(request))

        return when (val checkResult = idempotencyUseCase.check(key, hashRequest)) {
            is IdempotencyCheckResult.NotExists -> {
                idempotencyUseCase.saveProcessing(key, hashRequest)
                val result = joinPoint.proceed()
                idempotencyUseCase.saveCompleted(key, result)
                result
            }

            is IdempotencyCheckResult.Conflict -> throw IdempotencyConflictException()
            is IdempotencyCheckResult.Processing -> throw IdempotencyProcessingException()
            is IdempotencyCheckResult.Completed -> checkResult.result
        }
    }

    private fun generateKey(request: HttpServletRequest, idempotencyKey: String): String {
        val clientId = request.getHeader(GOODPON_CLIENT_ID_HEADER)!!
        val method = request.method
        val uri = request.requestURI

        val rawKey = "$idempotencyKey:$clientId:$method:$uri"
        return "idempotency:${HashUtil.hash(rawKey)}"
    }

    private fun getRequestBody(request: HttpServletRequest): String {
        return request.reader.use { it.readText() }
    }

    private fun validateIdempotencyKeyLength(idempotencyKey: String) {
        if (idempotencyKey.length > IDEMPOTENCY_KEY_LENGTH_LIMIT) {
            throw InvalidIdempotencyKeyException()
        }
    }

    companion object {
        private const val IDEMPOTENCY_KEY_HEADER = "Idempotency-Key"
        private const val GOODPON_CLIENT_ID_HEADER = "X-Goodpon-Client-Id"
        private const val IDEMPOTENCY_KEY_LENGTH_LIMIT = 300
    }
}