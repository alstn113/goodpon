package io.github.alstn113.goodpon.api.argumentresolver

enum class ApiKeyType(
    val prefixes: Set<String>,
) {
    CLIENT_KEY(setOf("live_ck_", "test_ck_")),
    SECRET_KEY(setOf("live_sk_", "test_sk_")),
    ;

    companion object {
        fun fromApiKey(apiKey: String): ApiKeyType {
            return entries.find { type -> type.prefixes.any { apiKey.startsWith(it) } }
                ?: throw IllegalArgumentException("Invalid API key: $apiKey")
        }
    }
}
