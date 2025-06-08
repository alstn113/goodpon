package io.github.alstn113.goodpon.domain.account

interface PasswordEncoder {

    fun encode(rawPassword: String): String

    fun matches(rawPassword: String, encodedPassword: String): Boolean
}