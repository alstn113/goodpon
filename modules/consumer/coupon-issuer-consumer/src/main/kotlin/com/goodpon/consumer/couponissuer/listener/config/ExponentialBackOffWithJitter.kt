package com.goodpon.consumer.couponissuer.listener.config

import org.springframework.util.backoff.BackOff
import org.springframework.util.backoff.BackOffExecution
import java.time.Duration
import java.util.Random

class ExponentialBackOffWithJitter(
    private val initialInterval: Duration,
    private val multiplier: Double,
    private val maxInterval: Duration,
    private val maxAttempts: Int,
) : BackOff {

    override fun start(): BackOffExecution {
        return object : BackOffExecution {
            private var currentInterval = initialInterval.toMillis()
            private var attempt = 0
            private val random = Random()

            override fun nextBackOff(): Long {
                if (attempt >= maxAttempts) {
                    return BackOffExecution.STOP
                }

                // 0.5 * base ~ 1.0 * base
                val jittered = currentInterval / 2 + random.nextLong(currentInterval / 2)
                currentInterval = (currentInterval * multiplier).toLong()
                if (currentInterval > maxInterval.toMillis()) {
                    currentInterval = maxInterval.toMillis()
                }
                attempt++

                return jittered
            }
        }
    }
}