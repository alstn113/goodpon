package com.goodpon.couponissuer.bootstrap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.couponissuer",
        "com.goodpon.infra"
    ]
)
class CouponIssuerApplication

fun main(args: Array<String>) {
    runApplication<CouponIssuerApplication>(*args)
}
