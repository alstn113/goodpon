package com.goodpon.partner.openapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.goodpon"])
class PartnerOpenApiApplication

fun main(args: Array<String>) {
    runApplication<PartnerOpenApiApplication>(*args)
}
