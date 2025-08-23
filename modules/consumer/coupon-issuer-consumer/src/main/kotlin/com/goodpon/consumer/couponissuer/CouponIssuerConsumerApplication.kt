package com.goodpon.consumer.couponissuer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.consumer.couponissuer",
        "com.goodpon.application.couponissuer",
        "com.goodpon.infra"
    ]
)
class CouponIssuerConsumerApplication

fun main(args: Array<String>) {
    runApplication<CouponIssuerConsumerApplication>(*args)
}
