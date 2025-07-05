package com.goodpon.core.application

import com.goodpon.core.domain.UniqueIdGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UuidUniqueIdGenerator : UniqueIdGenerator {

    override fun generate(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}