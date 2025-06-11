package com.goodpon.common.domain.merchant


fun interface SecretKeyGenerator {

    fun generate(): String
}