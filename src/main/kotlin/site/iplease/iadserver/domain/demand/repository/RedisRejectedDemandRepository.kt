package site.iplease.iadserver.domain.demand.repository

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.config.RejectedDemandProperties
import site.iplease.iadserver.domain.demand.data.entity.RejectedDemand

@Component
class RedisRejectedDemandRepository(
    private val redisTemplate: ReactiveRedisTemplate<String, RejectedDemand>,
    private val rejectedDemandProperties: RejectedDemandProperties
): RejectedDemandRepository {
    override fun insert(rejectedDemand: RejectedDemand): Mono<Void> =
        redisTemplate.opsForValue()
            .set(formatKey(rejectedDemand.demandId), rejectedDemand)
            .then()

    private fun formatKey(demandId: Long) = "${rejectedDemandProperties.redisKeyPrefix}_$demandId"
}