package com.goodpon.application.partner.idempotency.service

import com.fasterxml.jackson.databind.JsonNode

data class IdempotencyResponse(
    val status: Int,
    val headers: Map<String, List<String>>,
    val body: JsonNode,
)