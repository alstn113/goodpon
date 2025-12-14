package com.goodpon.partner.bootstrap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.partner",
        "com.goodpon.infra"
    ]
)
class PartnerApplication

fun main(args: Array<String>) {
    runApplication<PartnerApplication>(*args)
}
