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
            resultType = Long::class.java
        }
    }

    @Bean("reserveCouponScript")
    fun reserveCouponScript(): RedisScript<Long> {
        return DefaultRedisScript<Long>().apply {
            setScriptSource(ResourceScriptSource(ClassPathResource("scripts/coupon/reserve-coupon.lua")))
            resultType = Long::class.java
        }
    }

    @Bean("completeIssueCouponScript")
    fun completeIssueCouponScript(): RedisScript<Long> {
        return DefaultRedisScript<Long>().apply {
            setScriptSource(ResourceScriptSource(ClassPathResource("scripts/coupon/complete-issue-coupon.lua")))
            resultType = Long::class.java
        }
    }


    @Bean("redeemCouponScript")
    fun redeemCouponScript(): RedisScript<Long> {
        return DefaultRedisScript<Long>().apply {
            setScriptSource(ResourceScriptSource(ClassPathResource("scripts/coupon/redeem-coupon.lua")))
            resultType = Long::class.java
        }
    }
}