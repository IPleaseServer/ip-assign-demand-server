package site.iplease.iadserver.domain.demand.legacy.repository

import org.reactivestreams.Publisher
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.demand.legacy.data.entity.Demand

interface DemandRepository: ReactiveCrudRepository<Demand, Long> {
    fun deleteByIdentifier(identifier: Long): Mono<Void>
    fun findByIdentifier(identifier: Long): Mono<Demand>
    fun existsByIdentifier(identifier: Long): Mono<Boolean>

    @Deprecated(message = "DemandSaver.saveDemand()를 써주세요!")
    override fun <S : Demand> save(entity: S): Mono<S>
    @Deprecated(message = "DemandSaver.saveDemand()를 써주세요!")
    override fun <S : Demand> saveAll(entities: MutableIterable<S>): Flux<S>
    @Deprecated(message = "DemandSaver.saveDemand()를 써주세요!")
    override fun <S : Demand> saveAll(entityStream: Publisher<S>): Flux<S>
}