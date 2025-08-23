package com.goodpon.infra.redis.coupon.core

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.scripting.support.ResourceScriptSource


@Configuration
class RedisScriptConfig {

    @Bean("initStatsSetsScript")
    fun initStatsSetsScript(): RedisScript<Long> {
        return DefaultRedisScript<Long>().apply {
            setScriptSource(ResourceScriptSource(ClassPathResource("scripts/coupon/init-stats-sets.lua")))
            setResultType(Long::class.java)
        }
    }

    @Bean("issueCouponScript")
    fun issueCouponScript(): RedisScript<Long> {
        return DefaultRedisScript<Long>().apply {
            setScriptSource(ResourceScriptSource(ClassPathResource("scripts/coupon/issue-coupon.lua")))
            setResultType(Long::class.java)
        }
    }

    @Bean("redeemCouponScript")
    fun redeemCouponScript(): RedisScript<Long> {
        return DefaultRedisScript<Long>().apply {
            setScriptSource(ResourceScriptSource(ClassPathResource("scripts/coupon/redeem-coupon.lua")))
            setResultType(Long::class.java)
        }
    }
}