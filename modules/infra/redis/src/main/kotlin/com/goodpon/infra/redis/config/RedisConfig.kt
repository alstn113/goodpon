package com.goodpon.infra.redis.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisProperties::class)
class RedisConfig(
    private val properties: RedisProperties,
) {

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            keySerializer = StringRedisSerializer()
            valueSerializer = GenericJackson2JsonRedisSerializer(redisObjectMapper())
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()
            connectionFactory = lettuceConnectionFactory()
        }
    }

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(fromSerializer(GenericJackson2JsonRedisSerializer(redisObjectMapper())))
            .disableCachingNullValues()
            .entryTtl { _, _ -> Duration.ofDays(3) }

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(config)
            .build()
    }

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        return StringRedisTemplate().apply {
            connectionFactory = lettuceConnectionFactory()
        }
    }

    @Bean
    fun lettuceConnectionFactory(): LettuceConnectionFactory {
        val configuration = RedisStandaloneConfiguration().apply {
            hostName = properties.host
            port = properties.port
        }
        val clientConfigBuilder = LettuceClientConfiguration.builder()
        return LettuceConnectionFactory(configuration, clientConfigBuilder.build())
    }

    fun redisObjectMapper(): ObjectMapper {
        val validator = BasicPolymorphicTypeValidator.builder()
            .allowIfBaseType(Any::class.java)
            .build()

        return ObjectMapper()
            .registerKotlinModule()
            .registerModules(JavaTimeModule())
            .activateDefaultTyping(validator, DefaultTyping.EVERYTHING)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE) // 아무것도 직렬화 하지 않음
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY) // 필드만 직렬화 함
    }
}