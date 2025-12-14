package com.goodpon.dashboard.application.auth.port.out

interface PasswordEncoder {

    fun encode(rawPassword: String): String

    fun matches(rawPassword: String, encodedPassword: String): Boolean
}