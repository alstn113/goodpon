package com.goodpon.dashboard.api.controller.v1

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/v1/test")
    fun test(): ResponseEntity<TestResponse> {
        val response = TestResponse("안녕 😘")
        return ResponseEntity.ok(response)
    }

    data class TestResponse(
        val message: String,
    )
}