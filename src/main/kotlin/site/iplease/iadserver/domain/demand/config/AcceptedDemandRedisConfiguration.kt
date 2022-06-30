package site.iplease.iadserver.domain.demand.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import site.iplease.iadserver.domain.demand.data.entity.AcceptedDemand

@Configuration
class AcceptedDemandRedisConfiguration {
    @Bean
    fun acceptedDemandReactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory, objectMapper: ObjectMapper) =
        objectMapper.let { Jackson2JsonRedisSerializer(AcceptedDemand::class.java).apply { setObjectMapper(it) } }
            .let { StringRedisSerializer() to it }
            .let {
                RedisSerializationContext
                    .newSerializationContext<String, AcceptedDemand>()
                    .key(it.first).value(it.second)
                    .hashKey(it.first).hashValue(it.second)
                    .build()
            }.let { ReactiveRedisTemplate(connectionFactory, it) }
}