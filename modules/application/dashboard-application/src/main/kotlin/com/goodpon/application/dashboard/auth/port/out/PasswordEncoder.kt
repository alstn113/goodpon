package com.goodpon.application.dashboard.auth.port.out

interface PasswordEncoder {

    fun encode(rawPassword: String): String

    fun matches(rawPassword: String, encodedPassword: String): Boolean
}