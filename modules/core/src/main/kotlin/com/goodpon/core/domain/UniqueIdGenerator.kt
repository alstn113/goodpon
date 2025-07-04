package com.goodpon.core.domain

fun interface UniqueIdGenerator {
    fun generate(): String
}