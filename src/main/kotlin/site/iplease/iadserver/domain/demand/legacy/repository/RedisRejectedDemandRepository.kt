package site.iplease.iadserver.domain.demand.legacy.repository

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.config.RejectedDemandProperties
import site.iplease.iadserver.domain.demand.legacy.data.entity.RejectedDemand

@Component
class RedisRejectedDemandRepository(
    private val redisTemplate: ReactiveRedisTemplate<String, RejectedDemand>,
    private val rejectedDemandProperties: RejectedDemandProperties
): RejectedDemandRepository {
    override fun insert(rejectedDemand: RejectedDemand): Mono<Void> =
        redisTemplate.opsForValue()
            .set(formatKey(rejectedDemand.demandId), rejectedDemand)
            .then()

    override fun exist(id: Long): Mono<Boolean> = redisTemplate.hasKey(formatKey(id))
    override fun selectAll(): Flux<RejectedDemand> = ScanOptions.scanOptions()
        .match("${rejectedDemandProperties.redisKeyPrefix}_*")
        .count(100).build()
        .let { redisTemplate.scan(it) }
        .flatMap { key -> redisTemplate.opsForValue().get(key) }

    override fun delete(demandId: Long): Mono<Long> = redisTemplate.delete(formatKey(demandId))

    private fun formatKey(demandId: Long) = "${rejectedDemandProperties.redisKeyPrefix}_$demandId"
}