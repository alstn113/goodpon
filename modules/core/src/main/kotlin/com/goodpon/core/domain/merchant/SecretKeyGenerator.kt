package com.goodpon.core.domain.merchant


fun interface SecretKeyGenerator {
    fun generate(): String
}