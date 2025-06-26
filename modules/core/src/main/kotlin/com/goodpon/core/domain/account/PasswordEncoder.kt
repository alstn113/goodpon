package com.goodpon.core.domain.account

interface PasswordEncoder {

    fun encode(rawPassword: String): String
    fun matches(rawPassword: String, encodedPassword: String): Boolean
}