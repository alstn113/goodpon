package com.goodpon.infra.security.encoder

import com.goodpon.core.domain.account.PasswordEncoder
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