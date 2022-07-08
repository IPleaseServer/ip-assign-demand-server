package site.iplease.iadserver.domain.reject.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import site.iplease.iadserver.domain.reject.data.entity.RejectedDemand

@Configuration
class RejectedDemandRedisConfiguration {
    @Bean
    fun rejectedDemandReactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory, objectMapper: ObjectMapper) =
        objectMapper.let { Jackson2JsonRedisSerializer(RejectedDemand::class.java).apply { setObjectMapper(it) } }
            .let { StringRedisSerializer() to it }
            .let {
                RedisSerializationContext
                    .newSerializationContext<String, RejectedDemand>()
                    .key(it.first).value(it.second)
                    .hashKey(it.first).hashValue(it.second)
                    .build()
            }.let { ReactiveRedisTemplate(connectionFactory, it) }
}