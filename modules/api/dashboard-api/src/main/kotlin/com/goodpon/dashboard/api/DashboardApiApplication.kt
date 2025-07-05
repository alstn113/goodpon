package com.goodpon.dashboard.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.goodpon"])
class DashboardApiApplication

fun main(args: Array<String>) {
    runApplication<DashboardApiApplication>(*args)
}
