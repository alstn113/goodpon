package com.goodpon.partner.application.idempotency.service

import com.fasterxml.jackson.databind.JsonNode

data class IdempotencyResponse(
    val status: Int,
    val headers: Map<String, List<String>>,
    val body: JsonNode,
)