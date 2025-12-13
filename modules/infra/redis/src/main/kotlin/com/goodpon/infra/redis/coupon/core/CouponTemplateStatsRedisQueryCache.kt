package com.goodpon.infra.redis.coupon.core

import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_ISSUED_SET_KEY_PREFIX
import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_REDEEMED_SET_KEY_PREFIX
import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_RESERVED_SET_KEY_PREFIX
import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * 통계 조회용 Cache
 * - 발급 수(Issued) = (Issued Set Size - 1) + (Reserved ZSet Size - 1)
 * - 사용 수(Redeemed) = (Redeemed Set Size - 1)
 * * -1을 하는 이유는 init 시 생성한 dummy 값 제외를 위함.
 */
@Component
class CouponTemplateStatsRedisQueryCache(
    private val redisTemplate: StringRedisTemplate,
) {

    fun getStats(couponTemplateId: Long): Pair<Long, Long> {
        val reservedKey = CouponTemplateRedisKeyUtil.buildReservedSetKey(couponTemplateId) // [New]
        val issuedKey = CouponTemplateRedisKeyUtil.buildIssuedSetKey(couponTemplateId)
        val redeemedKey = CouponTemplateRedisKeyUtil.buildRedeemedSetKey(couponTemplateId)

        val issuedCount = (redisTemplate.opsForSet().size(issuedKey) ?: 1L) - 1
        val reservedCount = (redisTemplate.opsForZSet().size(reservedKey) ?: 1L) - 1
        val redeemedCount = (redisTemplate.opsForSet().size(redeemedKey) ?: 1L) - 1

        return (issuedCount + reservedCount) to redeemedCount
    }

    fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>> {
        val issuedKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildIssuedSetKey(it) }
        val reservedKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildReservedSetKey(it) }
        val redeemedKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildRedeemedSetKey(it) }

        return fetchStats(issuedKeys, reservedKeys, redeemedKeys, couponTemplateIds)
    }

    fun readAllStats(): Map<Long, Pair<Long, Long>> {
        val issuedKeys = redisTemplate.keys("$COUPON_ISSUED_SET_KEY_PREFIX*").toList()
        val reservedKeys = redisTemplate.keys("$COUPON_RESERVED_SET_KEY_PREFIX*").toList()
        val redeemedKeys = redisTemplate.keys("$COUPON_REDEEMED_SET_KEY_PREFIX*").toList()

        val allIds = (issuedKeys + reservedKeys + redeemedKeys)
            .map { it.substringAfterLast(":").toLong() }
            .distinct()

        val issuedKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildIssuedSetKey(it) }
        val reservedKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildReservedSetKey(it) }
        val redeemedKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildRedeemedSetKey(it) }

        return fetchStats(issuedKeyList, reservedKeyList, redeemedKeyList, allIds)
    }

    private fun fetchStats(
        issuedKeys: List<String>,
        reservedKeys: List<String>,
        redeemedKeys: List<String>,
        ids: List<Long>,
    ): Map<Long, Pair<Long, Long>> {
        if (ids.isEmpty()) return emptyMap()

        val results = pipelineForStats(issuedKeys, reservedKeys, redeemedKeys)
        val size = ids.size

        return ids.mapIndexed { i, id ->
            val issuedCount = results[i] - 1
            val reservedCount = results[i + size] - 1
            val redeemedCount = results[i + (size * 2)] - 1
            id to ((issuedCount + reservedCount) to redeemedCount)
        }.toMap()
    }

    private fun pipelineForStats(
        issuedKeys: List<String>,
        reservedKeys: List<String>,
        redeemedKeys: List<String>
    ): List<Long> {
        val results = redisTemplate.executePipelined { connection ->
            val stringCon = connection as StringRedisConnection
            issuedKeys.forEach { stringCon.sCard(it) }
            reservedKeys.forEach { stringCon.zCard(it) }
            redeemedKeys.forEach { stringCon.sCard(it) }
            null
        }
        return results.map { (it as? Long) ?: 1L }
    }
}