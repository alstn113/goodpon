package com.goodpon.infra.kafka.config

import org.springframework.util.backoff.BackOff
import org.springframework.util.backoff.BackOffExecution
import java.time.Duration
import kotlin.random.Random

class ExponentialBackOffWithJitter(
    private val initialInterval: Duration,
    private val multiplier: Double,
    private val maxElapsedTime: Duration,
    private val maxAttempts: Int,
): BackOff {

    override fun start(): BackOffExecution = Execution()

    private inner class Execution : BackOffExecution {
        private var currentInterval: Duration = initialInterval
        private var attemptCount: Int = 0
        private var elapsedTime: Duration = Duration.ZERO

        override fun nextBackOff(): Long {
            if (attemptCount >= maxAttempts || elapsedTime >= maxElapsedTime) {
                return BackOffExecution.STOP
            }

            val baseMillis = currentInterval.toMillis()

            // 0 ~ baseMillis
            val jitterMillis = if (baseMillis > 0) {
                Random.nextLong(0, baseMillis)
            } else {
                0L
            }

            val jitterDuration = Duration.ofMillis(jitterMillis)

            attemptCount++
            elapsedTime = elapsedTime.plus(jitterDuration)

            // 지수적 증가
            val nextIntervalMillis = (currentInterval.toMillis() * multiplier).toLong()
            currentInterval = Duration.ofMillis(
                nextIntervalMillis.coerceAtMost(Long.MAX_VALUE / 2)
            )

            return jitterDuration.toMillis()
        }
    }
}