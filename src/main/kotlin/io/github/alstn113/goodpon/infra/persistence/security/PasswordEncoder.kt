package io.github.alstn113.goodpon.infra.persistence.security

import io.github.alstn113.goodpon.domain.account.PasswordEncoder
import org.mindrot.jbcrypt.BCrypt
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