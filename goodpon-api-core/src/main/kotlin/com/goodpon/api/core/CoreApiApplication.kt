package com.goodpon.api.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages =
        [
            "com.goodpon.api.core",
            "com.goodpon.core",
            "com.goodpon.infra.db",
            "com.goodpon.infra.common",
            "com.goodpon.infra.security",
        ]
)
class CoreApiApplication

fun main(args: Array<String>) {
    runApplication<CoreApiApplication>(*args)
}
