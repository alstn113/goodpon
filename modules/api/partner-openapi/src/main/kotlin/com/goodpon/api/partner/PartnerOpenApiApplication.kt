package com.goodpon.api.partner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.api.partner",
        "com.goodpon.application.partner",
        "com.goodpon.infra"
    ]
)
class PartnerOpenApiApplication

fun main(args: Array<String>) {
    runApplication<PartnerOpenApiApplication>(*args)
}
