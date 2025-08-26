package com.goodpon.infra.redis.coupon.core

import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_TEMPLATE_STATS_ISSUE_SET_KEY_PREFIX
import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil.COUPON_TEMPLATE_STATS_REDEEM_SET_KEY_PREFIX
import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 *  Redis Set 은 비어 있을 경우 삭제되기 때문에 dummy 값을 하나 넣어둔다.
 *  이 dummy 값은 실제로는 사용되지 않기 때문에 Size 계산에서 -1을 해준다.
 */
@Component
class CouponTemplateStatsRedisQueryCache(
    private val redisTemplate: StringRedisTemplate,
) {

    fun getStats(couponTemplateId: Long): Pair<Long, Long> {
        val issueKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        val redeemKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)

        val issueCount = redisTemplate.opsForSet().size(issueKey) ?: 1L
        val redeemCount = redisTemplate.opsForSet().size(redeemKey) ?: 1L

        return issueCount - 1 to redeemCount - 1
    }

    fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>> {
        val issueKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildIssueSetKey(it) }
        val redeemKeys = couponTemplateIds.map { CouponTemplateRedisKeyUtil.buildRedeemSetKey(it) }
        return fetchStats(issueKeys, redeemKeys, couponTemplateIds)
    }

    fun readAllStats(): Map<Long, Pair<Long, Long>> {
        val issueKeys = redisTemplate.keys("$COUPON_TEMPLATE_STATS_ISSUE_SET_KEY_PREFIX*").toList()
        val redeemKeys = redisTemplate.keys("$COUPON_TEMPLATE_STATS_REDEEM_SET_KEY_PREFIX*").toList()

        val allIds = (issueKeys + redeemKeys)
            .map { it.substringAfterLast(":").toLong() }
            .distinct()

        val issueKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildIssueSetKey(it) }
        val redeemKeyList = allIds.map { CouponTemplateRedisKeyUtil.buildRedeemSetKey(it) }

        return fetchStats(issueKeyList, redeemKeyList, allIds)
    }

    private fun fetchStats(
        issueKeys: List<String>,
        redeemKeys: List<String>,
        ids: List<Long>,
    ): Map<Long, Pair<Long, Long>> {
        if (ids.isEmpty()) return emptyMap()

        val results = pipelineForSets(issueKeys, redeemKeys)
        return ids.mapIndexed { i, id ->
            id to (results[i] - 1 to results[i + ids.size] - 1)
        }.toMap()
    }

    private fun pipelineForSets(issueKeys: List<String>, redeemKeys: List<String>): List<Long> {
        val results = redisTemplate.executePipelined { connection ->
            val stringCon = connection as StringRedisConnection
            issueKeys.forEach { stringCon.sCard(it) }
            redeemKeys.forEach { stringCon.sCard(it) }
            null
        }
        return results.map { (it as? Long) ?: 1L }
    }
}