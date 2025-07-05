package com.goodpon.api.dashboard.security

import com.goodpon.core.application.auth.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BCryptPasswordEncoder : PasswordEncoder {

    override fun encode(rawPassword: String): String {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt())
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return BCrypt.checkpw(rawPassword, encodedPassword)
    }
}