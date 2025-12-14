package com.goodpon.dashboard.bootstrap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.dashboard",
        "com.goodpon.infra"
    ]
)
class DashboardApplication

fun main(args: Array<String>) {
    runApplication<DashboardApplication>(*args)
}
