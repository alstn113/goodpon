package io.github.alstn113.goodpon.api.argumentresolver

import io.github.alstn113.goodpon.application.merchant.MerchantService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.util.*

@Component
class ApiKeyArgumentResolver(
    private val merchantService: MerchantService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasApiKeyAnnotation = parameter.hasParameterAnnotation(ApiKey::class.java)
        val isLongClass = Long::class.java.isAssignableFrom(parameter.parameterType)

        return hasApiKeyAnnotation && isLongClass
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Long {
        val annotation = requireNotNull(parameter.getParameterAnnotation(ApiKey::class.java))
        val apiKey = extractApiKey(webRequest)
        val apiKeyType = ApiKeyType.fromApiKey(apiKey)

        if (apiKeyType != annotation.type) {
            throw IllegalArgumentException("Invalid API key type.")
        }

        return getMerchantIdByApiKey(apiKey, apiKeyType)
    }

    private fun extractApiKey(webRequest: NativeWebRequest): String {
        val authorizationHeader =
            webRequest.getHeader("Authorization")
                ?: throw IllegalArgumentException("Authorization header is missing")

        if (!authorizationHeader.startsWith("Basic ")) {
            throw IllegalArgumentException("Authorization header must start with 'Basic '")
        }

        val base64Credentials = authorizationHeader.substring(6)

        return try {
            String(Base64.getDecoder().decode(base64Credentials))
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid Base64 encoding in Authorization header", e)
        }
    }

    private fun getMerchantIdByApiKey(
        apiKey: String,
        apiKeyType: ApiKeyType,
    ): Long {
        return try {
            when (apiKeyType) {
                ApiKeyType.CLIENT_KEY -> merchantService.getMerchantByClientKey(apiKey).id
                ApiKeyType.SECRET_KEY -> merchantService.getMerchantBySecretKey(apiKey).id
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid API key: $apiKey", e)
        }
    }
}
