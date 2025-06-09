package io.github.alstn113.goodpon.api.goodpon.config

import io.github.alstn113.goodpon.api.goodpon.argumentresolver.ApiKeyArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val apiKeyArgumentResolver: ApiKeyArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(apiKeyArgumentResolver)
    }
}
