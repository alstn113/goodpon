package com.goodpon.api.partner.idempotency

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.partner.idempotency.IdempotencyUtils.IDEMPOTENCY_KEY_HEADER
import com.goodpon.api.partner.idempotency.IdempotencyUtils.validateIdempotencyKeyLength
import com.goodpon.api.partner.idempotency.exception.IdempotencyRequestPayloadMismatchException
import com.goodpon.api.partner.idempotency.exception.IdempotencyRequestProcessingException
import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets

@Aspect
@Component
class IdempotencyRequestAspect(
    private val idempotencyUseCase: IdempotencyUseCase,
    private val objectMapper: ObjectMapper,
) {

    @Around("@annotation(Idempotent)")
    fun around(joinPoint: ProceedingJoinPoint): Any {
        val request = (RequestContextHolder.currentRequestAttributes()
                as ServletRequestAttributes).request
                as ContentCachingRequestWrapper
        val response = (RequestContextHolder.currentRequestAttributes()
                as ServletRequestAttributes).response
                as ContentCachingResponseWrapper

        val idempotencyKey = request.getHeader(IDEMPOTENCY_KEY_HEADER)
        if (idempotencyKey.isNullOrBlank()) {
            return joinPoint.proceed()
        }

        response.setHeader(IDEMPOTENCY_KEY_HEADER, idempotencyKey)
        validateIdempotencyKeyLength(idempotencyKey)

        val key = IdempotencyUtils.generateKey(request, idempotencyKey)
        val hashRequest = IdempotencyUtils.hash(IdempotencyUtils.getRequestBody(request))

        return when (val checkResult = idempotencyUseCase.check(key, hashRequest)) {
            is IdempotencyCheckResult.NotExists -> {
                idempotencyUseCase.saveProcessing(key, hashRequest)
                return joinPoint.proceed()
            }

            is IdempotencyCheckResult.Conflict -> throw IdempotencyRequestPayloadMismatchException()
            is IdempotencyCheckResult.Processing -> throw IdempotencyRequestProcessingException()
            is IdempotencyCheckResult.Completed -> {
                val storedResponse = checkResult.response
                storedResponse.headers.forEach { (name, values) ->
                    values.forEach { value -> response.addHeader(name, value) }
                }
                response.status = storedResponse.status
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.characterEncoding = StandardCharsets.UTF_8.name()
                response.writer.write(objectMapper.writeValueAsString(storedResponse.body))
            }
        }
    }
}