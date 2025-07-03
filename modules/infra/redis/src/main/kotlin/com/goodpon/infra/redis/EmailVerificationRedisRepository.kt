package com.goodpon.infra.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.core.domain.auth.EmailVerification
import com.goodpon.core.domain.auth.EmailVerificationRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class EmailVerificationRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : EmailVerificationRepository {
    override fun save(verification: EmailVerification, ttlMinutes: Long) {
        val tokenKey = buildTokenKey(verification.token)
        val accountIdKey = buildAccountIdKey(verification.accountId)

        deletePrevToken(accountIdKey)

        redisTemplate.opsForValue().set(
            accountIdKey,
            verification.token,
            Duration.ofMinutes(ttlMinutes)
        )
        redisTemplate.opsForValue().set(
            tokenKey,
            objectMapper.writeValueAsString(verification),
            Duration.ofMinutes(ttlMinutes)
        )
    }

    override fun findByToken(token: String): EmailVerification? {
        val tokenKey = buildTokenKey(token)
        val verificationJson = redisTemplate.opsForValue().get(tokenKey)
            ?: return null

        return try {
            objectMapper.readValue(verificationJson, EmailVerification::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun delete(token: String, accountId: Long) {
        val tokenKey = buildTokenKey(token)
        val accountIdKey = buildAccountIdKey(accountId)

        redisTemplate.delete(tokenKey)
        redisTemplate.delete(accountIdKey)
    }

    private fun buildTokenKey(token: String): String {
        return "email-verification:token:$token"
    }

    private fun buildAccountIdKey(accountId: Long): String {
        return "email-verification:accountId:$accountId"
    }

    private fun deletePrevToken(accountIdKey: String) {
        val prevToken = redisTemplate.opsForValue().get(accountIdKey)
        if (prevToken != null) {
            redisTemplate.delete(buildTokenKey(prevToken))
        }
    }
}