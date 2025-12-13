package com.goodpon.infra.redis.coupon.core

import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_ISSUED_SET_KEY_PREFIX
import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_REDEEMED_SET_KEY_PREFIX
import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_RESERVED_ZSET_KEY_PREFIX
import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

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
        val reservedKey = CouponTemplateRedisKeyUtil.buildReservedZSetKey(couponTemplateId) // [New]
        val issuedKey = CouponTemplateRedisKeyUtil.buildIssuedSetKey(couponTemplateId)
        val redeemedKey = CouponTemplateRedisKeyUtil.buildRedeemedSetKey(couponTemplateId)

        val issuedCount = (redisTemplate.opsForSet().size(issuedKey) ?: 1L) - 1
        val reservedCount = (redisTemplate.opsForZSet().size(reservedKey) ?: 1L) - 1
        val redeemedCount = (redisTemplate.opsForSet().size(redeemedKey) ?: 1L) - 1

        return (issuedCount + reservedCount) to redeemedCount
    }

    fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>> {
        val issuedKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildIssuedSetKey(it) }
        val reservedKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildReservedZSetKey(it) }
        val redeemedKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildRedeemedSetKey(it) }

        return fetchStats(issuedKeys, reservedKeys, redeemedKeys, couponTemplateIds)
    }

    fun readAllStats(): Map<Long, Pair<Long, Long>> {
        val issuedIds = scanForIds("$COUPON_ISSUED_SET_KEY_PREFIX*")
        val reservedIds = scanForIds("$COUPON_RESERVED_ZSET_KEY_PREFIX*")
        val redeemedIds = scanForIds("$COUPON_REDEEMED_SET_KEY_PREFIX*")

        val allIds = (issuedIds + reservedIds + redeemedIds).toList()
        if (allIds.isEmpty()) {
            return emptyMap()
        }

        val issuedKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildIssuedSetKey(it) }
        val reservedKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildReservedZSetKey(it) }
        val redeemedKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildRedeemedSetKey(it) }

        return fetchStats(issuedKeyList, reservedKeyList, redeemedKeyList, allIds)
    }

    /**
     * 오래된 쿠폰 발급 예약 정보를 조회합니다.
     * 메시지가 발행되지 않고 Redis 에만 남아있는(좀비) 예약 정보를 찾는 데 사용됩니다.
     *
     * @param olderThan 기준 시간 (예: 5분. 즉, 5분 이상 지난 예약건 조회)
     * @return Map<couponTemplateId, Set<userId>>
     */
    fun getAllStaleCouponIssueReservations(olderThan: Duration): Map<Long, Set<String>> {
        val reservedIds = scanForIds("$COUPON_RESERVED_ZSET_KEY_PREFIX*")
        if (reservedIds.isEmpty()) {
            return emptyMap()
        }

        val maxScore = System.currentTimeMillis() - olderThan.toMillis()

        val results = redisTemplate.executePipelined { connection ->
            val conn = connection as StringRedisConnection
            for (id in reservedIds) {
                val key = CouponTemplateRedisKeyUtil.buildReservedZSetKey(id)
                conn.zRangeByScore(key, 1.0, maxScore.toDouble()) // dummy score = 0
            }
            null
        }

        val resultMap = mutableMapOf<Long, Set<String>>()
        reservedIds.toList().forEachIndexed { index, id ->
            @Suppress("UNCHECKED_CAST")
            val staleUsers = results[index] as? Set<String>

            if (!staleUsers.isNullOrEmpty()) {
                resultMap[id] = staleUsers
            }
        }

        return resultMap
    }

    private fun scanForIds(pattern: String): Set<Long> {
        val ids = mutableSetOf<Long>()
        val options = ScanOptions.scanOptions()
            .match(pattern)
            .count(1000)
            .build()

        redisTemplate.scan(options).use { cursor ->
            while (cursor.hasNext()) {
                val key = cursor.next()
                key.substringAfterLast(":").toLongOrNull()?.let { ids.add(it) }
            }
        }
        return ids
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