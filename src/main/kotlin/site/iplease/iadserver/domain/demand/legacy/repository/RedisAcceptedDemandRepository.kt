package site.iplease.iadserver.domain.demand.legacy.repository

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.config.AcceptedDemandProperties
import site.iplease.iadserver.domain.demand.legacy.data.entity.AcceptedDemand

@Component
class RedisAcceptedDemandRepository(
    private val redisTemplate: ReactiveRedisTemplate<String, AcceptedDemand>,
    private val acceptedDemandProperties: AcceptedDemandProperties
): AcceptedDemandRepository {
    override fun insert(acceptedDemand: AcceptedDemand): Mono<Void> =
        redisTemplate.opsForValue()
            .set(formatKey(acceptedDemand.demandId), acceptedDemand)
            .then()

    override fun exist(id: Long): Mono<Boolean> = redisTemplate.hasKey(formatKey(id))
    override fun selectAll(): Flux<AcceptedDemand> = ScanOptions.scanOptions()
        .match("${acceptedDemandProperties.redisKeyPrefix}_*")
        .count(100).build()
        .let { redisTemplate.scan(it) }
        .flatMap { key -> redisTemplate.opsForValue().get(key) }

    override fun delete(demandId: Long): Mono<Long> = redisTemplate.delete(formatKey(demandId))

    private fun formatKey(demandId: Long) = "${acceptedDemandProperties.redisKeyPrefix}_$demandId"
}